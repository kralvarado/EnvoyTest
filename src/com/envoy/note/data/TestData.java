package com.envoy.note.data;



public class TestData
{
    public ListCollection data;
    
    public static String[] LIST_NAMES = {
        "List1", "List2", "List3", "List4"
    };
    
    public static String[] NOTE_TEXT = {
        "Note1 text", "Note2 text", 
        "Note3 Note3 text should be very very long. With more than anything else in this entier list. Not sure how much text will be needed.", 
        "Note4 text", "Note5 text",
        "Note6 text", "Note7 text",
        "Note8 text", "Note9 text",
        "Note10 text", "Note11 text",
        "Note12 text", "Note13 text",
        "Note14 text", "Note15 text",
        "Note16 text", "Note17 text",
        "Note18 text", "Note19 text",
        "Note20 text", "Note21 text",
        "Note22 text", "Note23 text",
        "Note24 text", "Note25 text"
    };
    
    
    public TestData() {
        NoteCollection t_noteList = null;
        TextNote t_note = null;
        
        data= new ListCollection();
        
        int listCount = LIST_NAMES.length;
        int noteCount = NOTE_TEXT.length;
        
        for ( int i=0; i < listCount; i++ ) {
            t_noteList = new NoteCollection( LIST_NAMES[i] );
            for ( int j=0; j < noteCount; j++ ) {
                t_note = new TextNote( NOTE_TEXT[j] + " : " + LIST_NAMES[i] );
                t_noteList.addNote( t_note );
            }
            data.addList( t_noteList );
        }
    }
}
