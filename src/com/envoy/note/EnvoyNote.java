package com.envoy.note;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.envoy.note.data.TestData;



public class EnvoyNote extends Activity
{
    public static String FRAGMENT_NOTELIST_NAME = "F_NoteList";
    public static String FRAGMENT_NOTEVIEW_NAME = "F_NoteView";
    
    private NoteListFragment noteListFragment;
    private NoteViewFragment noteViewFragment;
    
    

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.notelist_activity_layout );
        TestData td = new TestData();
        
        FragmentManager fragmentManger = getFragmentManager();
        
        if ( fragmentManger.findFragmentById( android.R.id.content ) == null) {
            noteListFragment = new NoteListFragment( td.data.getList( 0 ));
            noteViewFragment = new NoteViewFragment();
            
            FragmentTransaction ft = fragmentManger.beginTransaction(); 
            ft.add( R.id.ListView, noteListFragment, FRAGMENT_NOTELIST_NAME );
            ft.add( R.id.NoteView, noteViewFragment, FRAGMENT_NOTEVIEW_NAME );
            ft.commit();
        }
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
    
    
    
    public void onDestroy() {
        super.onDestroy();
    }
    
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
    	boolean rValue = false;
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.envoynote_menu_actions, menu );
        rValue = super.onCreateOptionsMenu( menu );
        return rValue;
    }
}
