package com.envoy.note;

import java.util.ArrayList;

import com.envoy.note.data.ListItem;
import com.envoy.note.data.NoteItem;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
* ListViewFragment is used to display all notes within the currently selected list.
*/
public class ListViewFragment extends ListFragment
{
    private ListItem noteList;
    ArrayList<NoteItem> noteArrayList;
    private NoteItemAdapter noteListItemAdapter;
    
    

    public ListViewFragment() {
        noteList = null;
        // Initialize to a single 'empty' list
        noteArrayList = new ArrayList<NoteItem>();
        noteArrayList.add( new NoteItem( NoteItem.NO_ID, "empty" ));
    }
    
    
    
    public void setNoteCollection( ListItem notes ) {
        noteList = notes;
        noteArrayList = noteList.getArrayList();
        // Incase this fragment hasn't been viewed yet
        if ( noteListItemAdapter != null ) {
            noteListItemAdapter.clear();
            noteListItemAdapter.addAll( noteArrayList );
            noteListItemAdapter.notifyDataSetChanged();
        }
    }
    
    
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rView = null;

        noteListItemAdapter = new NoteItemAdapter( 
            inflater.getContext(), 
            R.layout.noteview_layout, 
            noteArrayList 
        );
        setListAdapter( noteListItemAdapter );
        rView = super.onCreateView( inflater, container, savedInstanceState );
        
        return rView;
    }
    
    
    
    // update listview and trigger display within NoteViewFragment
    private void updateView( int position ) {
        // trigger update of NoteViewFragment
        noteListItemAdapter.notifyDataSetChanged();
    }
}
