package com.envoy.note.data;



public class TextNote
{
    private int noteID;
    private String text;
    
    
    
    public TextNote( String s ) {
        noteID = -1;
        text = s;
    }
    
    
    
    public int getID() {
        return noteID;
    }
    
    
    
    public String getText() {
        return text;
    }
    
    
    
    public void setID( int id ) {
        noteID = id;
    }
    
    
    
    public void setText( String s ) {
        text = s;
    }
    
    
    
    public boolean equals( TextNote tn ) {
        return text.equals( tn.text );
    }
}
