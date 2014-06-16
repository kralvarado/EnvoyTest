package com.envoy.note;


import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.envoy.note.data.NoteCollection;



public class NoteListFragment extends ListFragment
{
    private NoteCollection noteList;
    
    
    
    public NoteListFragment( NoteCollection notes ) {
        noteList = notes;
    }
    
    
    
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rView = null;
        
        NoteListItemAdapter nlia = new NoteListItemAdapter( inflater.getContext(), R.layout.note_list_item_layout, noteList.getArrayList() );
        setListAdapter( nlia );
        rView = super.onCreateView( inflater, container, savedInstanceState );
        
        return rView;
    }
}
