package com.envoy.note;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.envoy.note.data.NoteItem;



/**
* Used by both ListViewFragment and NoteListFragment to populate 
* both lists with the NoteItem data 
*/
public class NoteItemAdapter extends ArrayAdapter<NoteItem>
{
    private int layoutID;
    
    
    
    public NoteItemAdapter( Context context, int resource, ArrayList<NoteItem> objects ) {
        super( context, resource, objects );
        layoutID = resource;
    }
    
    
    
    public View getView( int position, View convertView, ViewGroup parent ) {
        View rView = convertView;
        NoteItem t_note = getItem( position );
        
        if ( rView == null ) {
            rView = LayoutInflater.from( getContext() ).inflate( layoutID, parent, false );
        }
        
        TextView t_text = (TextView)rView.findViewById( R.id.NoteItem );
        t_text.setText( t_note.getText() );
        
        return rView;
    }
}
