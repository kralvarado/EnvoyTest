package com.envoy.note;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.envoy.note.data.ListCollection;
import com.envoy.note.data.TestData;
import com.envoy.note.network.AWSConnection;



public class EnvoyNote extends Activity implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener
{
    public static String FRAGMENT_NOTELIST_NAME = "F_NoteList";
    public static String FRAGMENT_NOTEVIEW_NAME = "F_NoteView";
    public static String FRAGMENT_LISTVIEW_NAME = "F_ListView";
    
    private NoteListFragment noteListFragment;  // fragment to show notes within a list, allows for selection to edit
    private NoteViewFragment noteViewFragment;  // shows selected note from noteListFragment
    private ListViewFragment listViewFragment;  // shows all notes within current list
    
    private Dialog newListDialog;
    private Dialog renameListDialog;
    private Dialog deleteListDialog;
    private EditText newListEditText;
    private EditText renameListEditText;
    private Button newListButton_OK;
    private Button newListButton_Cancel;
    private Button renameListButton_OK;
    private Button renameListButton_Cancel;
    private Button deleteListButton_OK;
    private Button deleteListButton_Cancel;
    
    private Spinner listNameSpinner;
    private ToggleButton viewAllToggle;
    
    private AWSConnection awsConnection;    // used to make server calls and fetch data
    private ListCollection listCollection;  // data-model 
    private TestData td;                    // test data used for development of UI
    
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.notelist_activity_layout );
        
        // ensure dialogs are null for proper initialization
        newListDialog = null;
        renameListDialog = null;
        deleteListDialog = null;
        
        td = new TestData();
        awsConnection = AWSConnection.getInstance();
        
        FragmentManager fragmentManger = getFragmentManager();
        
        if ( fragmentManger.findFragmentById( android.R.id.content ) == null) {
            noteListFragment = new NoteListFragment();
            noteViewFragment = new NoteViewFragment();
            listViewFragment = new ListViewFragment();
            
            noteListFragment.setNoteViewFragment( noteViewFragment );
            noteViewFragment.setNoteListFragment( noteListFragment );
            
            FragmentTransaction ft = fragmentManger.beginTransaction(); 
            ft.add( R.id.ListView, noteListFragment, FRAGMENT_NOTELIST_NAME );
            ft.add( R.id.NoteView, noteViewFragment, FRAGMENT_NOTEVIEW_NAME );
            ft.commit();
        }
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
    	boolean rValue = false;
    	MenuItem item;
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.envoynote_menu_actions, menu );
        
        // Add Spinner drop down into action bar menu
        item = menu.findItem( R.id.ActionSpinner );
        listNameSpinner = (Spinner)item.getActionView();
        listNameSpinner.setOnItemSelectedListener( this );
        
        // get ToggleButton reference
        item = menu.findItem( R.id.Action_ShowAll );
        viewAllToggle = (ToggleButton)item.getActionView();
        viewAllToggle.setTextOn( getResources().getString( R.string.IDS_EDIT_MODE ));
        viewAllToggle.setTextOff( getResources().getString( R.string.IDS_SHOW_ALL ));
        // set default button state text, prevents "Off" from showing before 1st click
        viewAllToggle.setText( getResources().getString( R.string.IDS_SHOW_ALL ));
        viewAllToggle.setOnCheckedChangeListener( this );
        
        rValue = super.onCreateOptionsMenu( menu );
        return rValue;
    }
    
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch ( item.getItemId() ) {
        case R.id.Action_ShowAll:
            showAllNotes();
            break;
        case R.id.Action_NewList:
            showNewDialog();
            break;
        case R.id.Action_RenameList:
            showRenameDialog();
            break;
        case R.id.Action_DeleteList:
            showDeleteDialog();
            break;
            
        case R.id.TEMP_START:
            loadListFromNetwork();
        }
        
        return true;
    }
    
    
    
    // Calls AWSConnection to get list names from server
    public void loadListFromNetwork() {
        listCollection = awsConnection.getLists();
        updateListSpinner();
    }
    
    
    
    private void updateListSpinner() {
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>( 
            getActionBar().getThemedContext(), 
            android.R.layout.simple_spinner_dropdown_item, 
            listCollection.getListNames()
        ); 
        listNameSpinner.setAdapter( spinnerAdapter );
    }
    
    
    
    public void onClick( View v ) {
        // ---- NEW LIST
        if ( v == newListButton_OK ) {
            String t_name = newListEditText.getText().toString();
            // verify that there's valid input
            // Non-empty string and doesn't contain any spaces
            // TODO: should check for non-valid url characters 
            // that cause POST to throw exception 
            if ( t_name != null && t_name.length() > 1 && !t_name.contains( " " )) {
                newListDialog.dismiss();
                awsConnection.createList( t_name );
                // update listCollection
                loadListFromNetwork();
            }
            else {
                String message = getResources().getString( R.string.IDS_INVALID_NAME );
                if ( t_name.contains( " " )) {
                    message = getResources().getString( R.string.IDS_CANNOT_CONTAIN_SPACES );
                }
                // display toast for invalid name
                Toast.makeText( 
                    getApplicationContext(),
                    message,
                    Toast.LENGTH_LONG
                ).show();
            }
        }
        else if ( v == newListButton_Cancel ) {
            newListDialog.dismiss();
        }
        
        // ---- RENAME LIST 
        else if ( v == renameListButton_OK ) {
            String t_name = renameListEditText.getText().toString();
            String listName = (String)listNameSpinner.getSelectedItem();
            int listID = listCollection.getListID( listName );
            
            if ( t_name != null && t_name.length() > 1 && !t_name.contains( " " )) {
                renameListDialog.dismiss();
                boolean success = awsConnection.updateList( listID, t_name );
                if ( success ) {
                    listCollection.getList( listID ).setName( t_name );
                    updateListSpinner();
                }
            }
            else {
                // display toast for invalid name
                String message = getResources().getString( R.string.IDS_INVALID_NAME );
                if ( t_name.contains( " " )) {
                    message = getResources().getString( R.string.IDS_CANNOT_CONTAIN_SPACES );
                }
                Toast.makeText( 
                    getApplicationContext(),
                    message,
                    Toast.LENGTH_LONG
                ).show();
            }
        }
        else if ( v == renameListButton_Cancel ) {
            renameListDialog.dismiss();
        }
        
        // ---- DELETE LIST
        else if ( v == deleteListButton_OK ) {
            String listName = (String)listNameSpinner.getSelectedItem();
            int listID = listCollection.getListID( listName );
            deleteListDialog.dismiss();
            boolean success = awsConnection.deleteList( listID );
            if ( success ) {
                // update listCollection
                loadListFromNetwork();
            }
        }
        else if ( v == deleteListButton_Cancel ) {
            deleteListDialog.dismiss();
        }
    }
    
    
    
    // List Selection Item Methods ----------------
    public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
        // would get the notes from server but I've been having trouble with the http parameters
        // when trying to create notes, I've been receiving 400 status for POST to list_items.php
        /*
        String listName = (String)listNameSpinner.getSelectedItem();
        int listID = listCollection.getListID( listName );
        noteListFragment.setNoteCollection( listCollection.getList( listID ));
        listViewFragment.setNoteCollection( listCollection.getList( listID ));
        */
        
        // For testing purposes of the UI these calls will populate
        // both fragments with fake data allowing the use of remove notes,
        // creating new notes, editing the notes, and showing all notes
        // update list data
        noteListFragment.setNoteCollection( td.data.getList( position ));
        listViewFragment.setNoteCollection( td.data.getList( position ));
    }
    
    
    public void onNothingSelected( AdapterView<?> parent ) {
        // do nothing
    }
    
    
    
    // ActionBar Action Items Methods ----------------
    public void showAllNotes() {
        FragmentManager fragmentManger = getFragmentManager();
        FragmentTransaction ft = fragmentManger.beginTransaction();
        if ( viewAllToggle.isChecked() ) { 
            ft.replace( R.id.NoteView, listViewFragment, FRAGMENT_LISTVIEW_NAME );
        }
        else {
            ft.replace( R.id.NoteView, noteViewFragment, FRAGMENT_NOTEVIEW_NAME );
        }
        ft.commit();
    }
    
    
    
    // ToggleButton action method ----------------
    public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
        // We know there's only viewAllToggle button so no need to test which buttonView
        
        FragmentManager fragmentManger = getFragmentManager();
        FragmentTransaction ft = fragmentManger.beginTransaction();
        if ( isChecked ) { 
            ft.replace( R.id.NoteView, listViewFragment, FRAGMENT_LISTVIEW_NAME );
        }
        else {
            ft.replace( R.id.NoteView, noteViewFragment, FRAGMENT_NOTEVIEW_NAME );
        }
        ft.commit();
    }
    
    
    
    public void showNewDialog() {
        if ( newListDialog == null ) {
            newListDialog = new Dialog( this );
            newListDialog.setContentView( R.layout.edit_dialog_layout );
            newListDialog.setTitle( R.string.IDS_CREATE_NEW_LIST );
            
            newListEditText = (EditText)newListDialog.findViewById( R.id.DialogEditText );
            newListEditText.setHint( R.string.IDS_ENTER_LIST_NAME );
            // t_editText - setEditListener
            // set alpha only but allow spaces

            newListButton_OK = (Button)newListDialog.findViewById( R.id.Button_OK );
            newListButton_OK.setText( R.string.IDS_OK );
            newListButton_OK.setOnClickListener( this );
            
            newListButton_Cancel = (Button)newListDialog.findViewById( R.id.Button_Cancel );
            newListButton_Cancel.setText( R.string.IDS_CANCEL );
            newListButton_Cancel.setOnClickListener( this );
        }
        
        newListEditText.setText( "" );
        newListDialog.show();
    }
    
    
    
    public void showRenameDialog() {
        if ( renameListDialog == null ) {
            StringBuffer t_title = new StringBuffer();
            t_title.append( getResources().getString( R.string.IDS_RENAME_LIST ));
            t_title.append( getResources().getString( R.string.IDS_COLON ));
            t_title.append( getResources().getString( R.string.IDS_SPACE ));
            t_title.append( (String)listNameSpinner.getSelectedItem() );
            t_title.append( getResources().getString( R.string.IDS_QUESTIONMARK ));

            renameListDialog = new Dialog( this );
            renameListDialog.setContentView( R.layout.edit_dialog_layout );
            renameListDialog.setTitle( R.string.IDS_RENAME_LIST );
            renameListDialog.setTitle( t_title.toString() );
            
            renameListEditText = (EditText)renameListDialog.findViewById( R.id.DialogEditText );
            renameListEditText.setHint( R.string.IDS_ENTER_NEW_LIST_NAME );
            // t_editText - setEditListener
            // set alpha only but allow spaces
            
            renameListButton_OK = (Button)renameListDialog.findViewById( R.id.Button_OK );
            renameListButton_OK.setText( R.string.IDS_OK );
            renameListButton_OK.setOnClickListener( this );
            
            renameListButton_Cancel = (Button)renameListDialog.findViewById( R.id.Button_Cancel );
            renameListButton_Cancel.setText( R.string.IDS_CANCEL );
            renameListButton_Cancel.setOnClickListener( this );
        }
        
        renameListEditText.setText( "" );
        renameListDialog.show();
    }
    
    
    
    public void showDeleteDialog() {
        TextView t_textView = null;
        if ( deleteListDialog == null ) {
            // for localization purposes, better to have method to construct
            // title question based upon language, as not all languages
            // same format as English: Delete "Name"?
            // ie, Spanish: ?Delete "Name"?
            StringBuffer t_title = new StringBuffer();
            t_title.append( getResources().getString( R.string.IDS_DELETE ));
            t_title.append( getResources().getString( R.string.IDS_COLON ));
            t_title.append( getResources().getString( R.string.IDS_SPACE ));
            t_title.append( (String)listNameSpinner.getSelectedItem() );
            t_title.append( getResources().getString( R.string.IDS_QUESTIONMARK ));
            
            deleteListDialog = new Dialog( this );
            deleteListDialog.setContentView( R.layout.confirm_dialog_layout );
            deleteListDialog.setTitle( t_title.toString() );
            
            t_textView = (TextView)deleteListDialog.findViewById( R.id.DialogWarningText );
            t_textView.setText( R.string.IDS_THIS_CANNOT_BE_UNDONE );
            // t_editText - setEditListener
            // set alpha only but allow spaces
            
            deleteListButton_OK = (Button)deleteListDialog.findViewById( R.id.Button_OK );
            deleteListButton_OK.setText( R.string.IDS_OK );
            deleteListButton_OK.setOnClickListener( this );
            
            deleteListButton_Cancel = (Button)deleteListDialog.findViewById( R.id.Button_Cancel );
            deleteListButton_Cancel.setText( R.string.IDS_CANCEL );
            deleteListButton_Cancel.setOnClickListener( this );
        }
        deleteListDialog.show();        
    }
}
