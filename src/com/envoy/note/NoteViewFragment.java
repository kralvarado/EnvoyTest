package com.envoy.note;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.envoy.note.data.NoteItem;
import com.envoy.note.network.AWSConnection;


/**
* Used to edit the Note's content.  The edits will not be saved until
* the 'Save Changes' button is used. 
*/
public class NoteViewFragment extends Fragment implements OnClickListener
{
    private NoteItem noteData;
    private EditText editTextView;
    private Button editDoneButton;
    private NoteListFragment noteListFragment;
    
    
    public NoteViewFragment() {
        noteData = null;
        editTextView = null;
        noteListFragment = null;
    }
    
    
    
    public void setNoteListFragment( NoteListFragment nlf ) {
        noteListFragment = nlf;
    }
    
    
    
    public void setNoteData( NoteItem tn ) {
        noteData = tn;
        if ( editTextView != null ) {
            editTextView.setText( noteData.getText() );
            editTextView.setEnabled( true );
            editDoneButton.setEnabled( true );
        }
    }
    
    
    
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rView = inflater.inflate( R.layout.note_view_fragment_layout, container, false );
        editTextView = (EditText)rView.findViewById( R.id.NoteViewEditText );
        editDoneButton = (Button)rView.findViewById( R.id.EditViewDoneButton );
        editDoneButton.setOnClickListener( this );
        // we're making assumption that a TextNote with id of -1 
        // is the 'empty' TextNote used to initialize ListViewFragment
        // so to prevent 'Saving' or editing an empty note, we disable UI
        // to be set when we get a TextNote when user selects from ListViewFragment
        boolean hasData = false;
        if ( noteData != null && noteData.getID() >= 0 ) {
            editTextView.setText( noteData.getText() );
            hasData = true;
        }
        editTextView.setEnabled( hasData );
        editDoneButton.setEnabled( hasData );
        
        return rView;
    }
    
    
    
    public void onDestroyView() {
        // clean up UI elements
        editTextView = null;
        editDoneButton = null;
        super.onDestroyView();
    }
    
    
    
    // Notify update to NoteListFragment
    public void onClick( View v ) {
        String t_editText = editTextView.getText().toString();
        // ensure text has changed, AND is not empty
        // ignore this action on 'save' if note's text has not changed
        if ( v == editDoneButton && !t_editText.equals( noteData.getText() ) && t_editText.length() > 0 ) {
            noteData.setText( t_editText );
            noteListFragment.updateListItemText();
            
            // update note on server
            AWSConnection.getInstance().updateNote( noteData.getID(), t_editText );
        }
        else if ( t_editText.length() == 0 ) {
            // display toast for invalid note
            Toast.makeText( 
                getActivity(),
                getResources().getString( R.string.IDS_INVALID_NOTE ),
                Toast.LENGTH_LONG
            ).show();
        }
    }
}
