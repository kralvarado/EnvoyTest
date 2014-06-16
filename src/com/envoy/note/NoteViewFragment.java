package com.envoy.note;

import android.app.Fragment;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;



public class NoteViewFragment extends Fragment
{
    
    
    
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
    }
    
    
    
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState ); 
    }
    
    
    
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rView = inflater.inflate( R.layout.note_view_fragment_layout, container, false );
        EditText ev = (EditText)rView.findViewById( R.id.NoteViewEditText );
        ev.setText( "This is my note view" );
        return rView;
    }
    
    
    
    public void onActivityCreated( Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
    }
    
    
    
    public void onStart() {
        super.onStart();
    }
    
    
    
    public void onResume() {
        super.onResume();
    }
    
    
    
    public void onPause() {
        super.onPause();
    }
    
    
    
    public void onStop() {
        super.onStop();
    }
    
    
    
    public void onDestroyView() {
        super.onDestroyView();
    }
    
    
    
    public void onDestroy() {
        super.onDestroy();
    }
    
    
    
    public void onDetach() {
        super.onDetach();
    }
    
    
    
    public void onInflate( Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate( activity, attrs, savedInstanceState );
    }
}
