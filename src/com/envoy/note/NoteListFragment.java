package com.envoy.note;


import java.util.ArrayList;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.envoy.note.data.ListItem;
import com.envoy.note.data.NoteItem;
import com.envoy.note.network.AWSConnection;


/**
* NoteListFragment is the left side panel, used to show each NoteItem
* selecting an item will notify NoteViewFragment to display the note's content
* A long-press will delete the pressed note
*/
public class NoteListFragment extends ListFragment implements OnClickListener, OnItemLongClickListener
{
    private ListItem noteList;
    private ArrayList<NoteItem> noteArrayList;      // used to hold Notes, separate from noteList (data-model)
    private NoteItemAdapter noteListItemAdapter;
    private FrameLayout buttonLayout;
    private Button addButton;
    private int currentPosition;
    
    private NoteViewFragment noteViewFragment;
    
    
    
    public NoteListFragment() {
        noteList = null;
        // Initialize to a single 'empty' list
        noteArrayList = new ArrayList<NoteItem>();
        noteArrayList.add( new NoteItem( NoteItem.NO_ID, "empty" ));
        addButton = null;
        currentPosition = -1;
    }
    
    
    
    public void setNoteViewFragment( NoteViewFragment nvf ) {
        noteViewFragment = nvf;
        if ( noteArrayList != null ) {
            noteViewFragment.setNoteData( noteArrayList.get( 0 ));
        }
        currentPosition = 0;
    }
    
    
    
    public void setNoteCollection( ListItem notes ) {
        noteList = notes;
        noteArrayList = noteList.getArrayList();
        if ( noteArrayList.size() > 0 ) {
            noteViewFragment.setNoteData( noteArrayList.get( 0 ));
            currentPosition = 0;
            noteListItemAdapter.clear();
            noteListItemAdapter.addAll( noteArrayList );
            updateView( 0 );
        }
        addButton.setEnabled( true );
    }
    
    
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rView = null;

        noteListItemAdapter = new NoteItemAdapter( 
            inflater.getContext(), 
            R.layout.note_list_item_layout, 
            noteArrayList
        );
        setListAdapter( noteListItemAdapter );
        rView = super.onCreateView( inflater, container, savedInstanceState );
        
        buttonLayout = (FrameLayout)inflater.inflate( R.layout.add_note_button, null);
        addButton = (Button) buttonLayout.findViewById( R.id.AddNoteButton );
        addButton.setOnClickListener( this );
        if ( noteList == null ) {
            addButton.setEnabled( false );
        }
        
        return rView;
    }
    
    
    
    @Override
    public void onActivityCreated( Bundle savedInstanceState ) {
        this.getView().requestLayout();
        
        ListView lv = getListView();
        lv.addFooterView( buttonLayout );
        lv.setOnItemLongClickListener( this );
        
        super.onActivityCreated( savedInstanceState );
    }
    
    
    
    public void onListItemClick( ListView l, View v, int position, long id ) {
        currentPosition = position;
        updateView( position );
    }
    
    
    
    // Remove Note from data
    public boolean onItemLongClick( AdapterView<?> parent, View view, int position, long id ) {
        NoteItem t_note = noteArrayList.get( position );
        int t_noteID = t_note.getID();
        noteList.removeNote( position );
        noteArrayList.remove( position );
        //AWSConnection.getInstance().deleteNote( t_noteID );
        noteListItemAdapter.remove( t_note );
        
        // check if deleting current
        int updatePos = position;
        if ( currentPosition == position ) {
            currentPosition--;
            if ( currentPosition < 0 ) {
                currentPosition = 0;
            }
            updatePos = currentPosition;
        }
        updateView( updatePos );

        return true;
    }
    
    
    
    public void onClick( View v ) {
        if ( v == addButton ) {
            NoteItem tn = new NoteItem( NoteItem.NO_ID, getResources().getString( R.string.IDS_NEW_NOTE_TEXT ));
            noteList.addNote( tn );
            noteArrayList.add( tn );
            noteListItemAdapter.add( tn );
            updateView( noteList.getCount() -1 );
            
            //AWSConnection.getInstance().createNote( noteList.getID(), tn.getText() );
        }
    }
    
    
    
    // update listview and trigger display within NoteViewFragment
    private void updateView( int position ) {
        // trigger update of NoteViewFragment
        noteListItemAdapter.notifyDataSetChanged();
        noteViewFragment.setNoteData( noteArrayList.get( position ));
    }
    
    
    
    public void updateListItemText() {
        noteListItemAdapter.notifyDataSetChanged();
    }
}
