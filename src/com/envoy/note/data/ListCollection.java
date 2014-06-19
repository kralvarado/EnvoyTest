package com.envoy.note.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


/**
* ListCollection is to hold a list of lists.  Mainly the 'root' of 
* the data-model.  It's also used to build the Spinner content. 
*/
public class ListCollection
{
    private HashMap<Integer, ListItem> listData;
    
    
    
    public ListCollection() {
        listData = new HashMap<Integer, ListItem>();
    }
    
    
    
    public ArrayList<ListItem> getLists() {
        ArrayList<ListItem> lists = new ArrayList<ListItem>( listData.values() );
        return lists;
    }
    
    
    
    public ArrayList<String> getListNames() {
        ArrayList<String> names = new ArrayList<String>();
        Vector<ListItem> lists = new Vector<ListItem>( listData.values() );
        
        String t_name = null;
        for ( int i=0; i < lists.size(); i++ ) {
            t_name = lists.get( i ).getName();
            names.add( t_name );
        }
        
        return names;
    }
    
    
    
    public void addList( ListItem list ) {
        Integer key = Integer.valueOf( list.getID() );
        listData.put( key, list );
    }
    
    
    
    public ListItem getList( int id ) {
        ListItem rList = listData.get( id );
        return rList;
    }
    
    
    
    public void removeList( int id ) {
        listData.remove( id );
    }
    
    
    
    // returns -1 if name is not found
    public int getListID( String name ) {
        int rValue = -1;
        Vector<ListItem> lists = new Vector<ListItem>( listData.values() );
        
        ListItem t_item = null;
        for ( int i=0; i < lists.size(); i++ ) {
            t_item = lists.elementAt( i );
            if ( t_item.getName().equals( name ) ) {
                // we've found a match jump out of loop
                // I don't like to break from for loops 
                // but, due to time constraints, here we go.
                rValue = t_item.getID();
                break;
            }
        }
        return rValue;
    }
}
