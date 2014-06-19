package com.envoy.note.data;



/**
* Simple note.  Holds a String used as the note's text   
*/
public class NoteItem
{
    public static final int NO_ID = -1;
    
    private int listID;
    private int noteID;
    private String text;
    
    
    
    public NoteItem( int id, String s ) {
        listID = -1;
        noteID = id;
        text = s;
    }
    
    
    
    public int getListID() {
        return listID;
    }
    
    
    
    public int getID() {
        return noteID;
    }
    
    
    
    public String getText() {
        return text;
    }
    
    
    
    public void setListID( int id ) {
        listID = id;
    }
    
    
    
    public void setID( int id ) {
        noteID = id;
    }
    
    
    
    public void setText( String s ) {
        text = s;
    }
    
    
    
    public boolean equals( NoteItem tn ) {
        boolean rValue = false;
        int count = 0;
        if ( text.equals( tn.text )) count++;
        if ( noteID == tn.noteID ) count++;
        if ( count == 2 ) rValue = true;
        return rValue;
    }
}
