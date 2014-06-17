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

import com.envoy.note.data.NoteCollection;
import com.envoy.note.data.TextNote;



public class NoteListFragment extends ListFragment implements OnClickListener, OnItemLongClickListener
{
    private NoteCollection noteList;
    private ArrayList<TextNote> noteArrayList;
    private NoteListItemAdapter noteListItemAdapter;
    private FrameLayout buttonLayout;
    private Button addButton;
    private int currentPosition;
    
    private NoteViewFragment noteViewFragment;
    
    
    
    public NoteListFragment( NoteCollection notes ) {
        noteList = notes;
        noteArrayList = noteList.getArrayList();
        addButton = null;
        currentPosition = -1;
    }
    
    
    
    public void setNoteViewFragment( NoteViewFragment nvf ) {
        noteViewFragment = nvf;
        noteViewFragment.setNoteData( noteArrayList.get( 0 ));
        currentPosition = 0;
    }
    
    
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rView = null;
        
        noteListItemAdapter = new NoteListItemAdapter( inflater.getContext(), R.layout.note_list_item_layout, noteArrayList );
        setListAdapter( noteListItemAdapter );
        rView = super.onCreateView( inflater, container, savedInstanceState );
        //rView = inflater.inflate( R.layout.notelist_fragment_layout, null);
        
        buttonLayout = (FrameLayout)inflater.inflate( R.layout.add_note_button, null);
        addButton = (Button) buttonLayout.findViewById( R.id.AddNoteButton );
        addButton.setOnClickListener( this );
        
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
        noteList.removeNote( position );
        noteArrayList.remove( position );
        
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
            TextNote tn = new TextNote( "new" );    // TODO:use string reference for localization
            noteList.addNote( tn );
            noteArrayList.add( tn );
            updateView( noteList.getCount() -1 );
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
