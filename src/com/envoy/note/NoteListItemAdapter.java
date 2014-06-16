package com.envoy.note;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.envoy.note.data.TextNote;



public class NoteListItemAdapter extends ArrayAdapter<TextNote>
{
    //private ArrayList<TextNote> listNoteItems;
    
    
    
    public NoteListItemAdapter( Context context, int resource, ArrayList<TextNote> objects ) {
        super( context, resource, objects );
    }
    
    
    
    public View getView( int position, View convertView, ViewGroup parent ) {
        View rView = convertView;
        TextNote tnote = getItem( position );
        
        if ( rView == null ) {
            rView = LayoutInflater.from( getContext() ).inflate( R.layout.note_list_item_layout, parent, false );
        }
        
        TextView t_text = (TextView)rView.findViewById( R.id.NoteItem );
        t_text.setText( tnote.getText() );
        
        return rView;
    }
}
