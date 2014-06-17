package com.envoy.note;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.envoy.note.data.TextNote;



public class NoteViewFragment extends Fragment implements OnClickListener
{
    private TextNote noteData;
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
    
    
    
    public void setNoteData( TextNote tn ) {
        noteData = tn;
        if ( editTextView != null ) {
            editTextView.setText( noteData.getText() );
        }
    }
    
    
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rView = inflater.inflate( R.layout.note_view_fragment_layout, container, false );
        editTextView = (EditText)rView.findViewById( R.id.NoteViewEditText );
        editTextView.setText( noteData.getText() );
        editDoneButton = (Button)rView.findViewById( R.id.EditViewDoneButton );
        editDoneButton.setOnClickListener( this );
        return rView;
    }
    
    
    
    // Notify update to NoteListFragment
    public void onClick( View v ) {
        String t_editText = editTextView.getText().toString();
        if ( v == editDoneButton && !t_editText.equals( noteData.getText() )) {
            noteData.setText( t_editText );
            noteListFragment.updateListItemText();
        }
    }
}
