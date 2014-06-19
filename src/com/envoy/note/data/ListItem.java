package com.envoy.note.data;

import java.util.ArrayList;
import java.util.HashMap;


/**
* ListItem is a collection of NoteItems. Used to build up
* both View-All-Notes view, and the list-view on left screen.
*/
public class ListItem
{
    private int listID;
    private String listName;
    private HashMap<Integer, NoteItem> noteData;
    
    
    
    public ListItem( int id, String name ) {
        listID = id;
        listName = name;
        noteData = new HashMap<Integer, NoteItem>();
    }
    
    
    
    public int getID() {
        return listID;
    }
    
    
    
    public String getName() {
        return listName;
    }
    
    
    
    public int getCount() {
        return noteData.size();
    }
    
    
    
    public void setID( int id ) {
        listID = id;
    }
    
    
    
    public void setName( String s ) {
        listName = s;
    }
    
    
    
    public ArrayList<NoteItem> getArrayList() {
        ArrayList<NoteItem> notes = new ArrayList<NoteItem>();
        
        for ( int i=0; i < noteData.size(); i++ ) {
            notes.add( noteData.get( i ));
        }
        
        return notes;
    }
    
    
    
    public NoteItem getNote( int id ) {
        return noteData.get( id );
    }
    
    
    
    public void addNote( NoteItem note ) {
        Integer key = Integer.valueOf( note.getID() );
        noteData.put( key, note );
    }
    
    
    
    public void removeNote( int id ) {
        noteData.remove( id );
    }
    
    
    
    
    //public boolean equals( ListItem nc ) {
    //    boolean rValue = false;
    //    if ( listID == nc.listID ) rValue = true;
    //    return rValue;
    //}
}
