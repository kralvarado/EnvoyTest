package com.envoy.note.data;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;



public class NoteCollection
{
    private int listID;
    private String listName;
    private Vector<TextNote> noteData;
    
    
    
    public NoteCollection( String name ) {
        listID = -1;
        listName = name;
        noteData = new Vector<TextNote>();
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
    
    
    
    public Iterator<TextNote> getData() {
        return noteData.iterator();
    }
    
    
    public ArrayList<TextNote> getArrayList() {
        return new ArrayList<TextNote>( noteData );
    }
    
    
    
    public TextNote getNote( int id ) {
        TextNote rNote = null;
        rNote = noteData.elementAt( id );
        return rNote;
    }
    
    
    
    public void addNote( TextNote note ) {
        note.setID( noteData.size() );
        noteData.add( note );
    }
    
    
    
    public void removeNote( int id ) {
        noteData.removeElementAt( id );
    }
    
    
    
    public boolean equals( NoteCollection nc ) {
        boolean rValue = false;
        if ( listID == nc.listID ) rValue = true;
        return rValue;
    }
}
