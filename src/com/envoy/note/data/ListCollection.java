package com.envoy.note.data;

import java.util.Iterator;
import java.util.Vector;



public class ListCollection
{
    private Vector<NoteCollection> listData;
    
    
    
    public ListCollection() {
        listData = new Vector<NoteCollection>();
    }
    
    
    
    public Iterator<NoteCollection> getData() {
        return listData.iterator();
    }
    
    
    
    public void addList( NoteCollection nc ) {
        nc.setID( listData.size() );
        listData.add( nc );
    }
    
    
    
    public NoteCollection getList( int id ) {
        NoteCollection rList = null;
        rList = listData.elementAt( id );
        return rList;
    }
    
    
    
    public void removeList( int id ) {
        listData.removeElementAt( id );
    }
}
