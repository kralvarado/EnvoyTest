package com.envoy.note.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.envoy.note.data.ListCollection;
import com.envoy.note.data.ListItem;



/**
* Singleton used to handle all server calls to the AmazonWebServer
* Is to handle all Http Requests and provide the requested Lists & Notes
* NOTE:
*   Due to time constraints this class is not complete.
*/
public class AWSConnection
{
    private static String BASEURL = "http://ec2-54-213-150-7.us-west-2.compute.amazonaws.com";
    private static String PAGE_LIST = "/lists.php";
    private static String PAGE_ITEMS = "/list_items.php";

    public static String KEY_ID = "id";
    public static String KEY_LISTID = "list_id";
    public static String KEY_NAME = "name";
    public static String KEY_NOTE = "note";
    
    private static AWSConnection instance;
    
    private static BasicHttpParams httpParams;
    private static DefaultHttpClient httpClient;
    private static HttpEntity httpEntity;
    private static HttpDelete httpDelete;
    private static HttpGet httpGet;
    private static HttpPost httpPost;
    private static HttpPut httpPut;
    private static HttpResponse httpResponse;
    private static StatusLine statusLine;
    private static int statusCode;
    
    
    
    private AWSConnection() {
        // nothing to do
    }
    
    
    
    public static AWSConnection getInstance() {
        if ( instance == null ) {
            instance = new AWSConnection();
            
            httpClient = new DefaultHttpClient();
        }
        return instance;
    }
    
    
    
    // returns a list of ListItem objects
    public ListCollection getLists() {
        // GET ./lists.php
        // build URL ./lists.php
        final StringBuffer t_buffer = new StringBuffer();
        t_buffer.append( BASEURL );
        t_buffer.append( PAGE_LIST );
        
        // send request
        // based off code from:
        // http://www.vogella.com/tutorials/AndroidJSON/article.html
        Thread t_getList = new Thread() {
            public void run() {
                try {
                    httpGet = new HttpGet( t_buffer.toString() );
                    httpResponse = httpClient.execute( httpGet );
                    statusLine = httpResponse.getStatusLine();
                    statusCode = statusLine.getStatusCode();
                    
                    if ( statusCode == 200 ) {
                        httpEntity = httpResponse.getEntity();
                        InputStream content = httpEntity.getContent();
                        BufferedReader reader = new BufferedReader( new InputStreamReader( content ));
                        String t_line = reader.readLine();
                        t_buffer.setLength( 0 );
                        while ( t_line != null ) {
                            t_buffer.append( t_line );
                            t_line = reader.readLine();
                        }
                    }
                }
                catch ( ClientProtocolException cpe ) {
                    cpe.printStackTrace();
                }
                catch ( IOException ioe ) {
                    ioe.printStackTrace();
                }
            }
        };
        
        t_getList.start();
        try {
            t_getList.join();
        }
        catch ( InterruptedException ie ) {
            ie.printStackTrace();
        }
        
        // build and add list of lists
        ListCollection listCollection = new ListCollection();
        try {
            ListItem listItem = null;
            int id;
            String name;
            JSONArray jArray = new JSONArray( t_buffer.toString() );
            for ( int i=0; i < jArray.length(); i++ ) {
                JSONObject jObject = jArray.getJSONObject( i );
                id = jObject.getInt( KEY_ID );
                name = jObject.getString( KEY_NAME );
                listItem = new ListItem( id, name );
                listCollection.addList( listItem );
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
        
        return listCollection;
    }
    
    
    
    // returns a single list name based upon listID
    public void getList( int listID ) {
        // GET ./lists.php?id=<listID>
        final StringBuffer t_buffer = new StringBuffer();
        t_buffer.append( BASEURL );
        t_buffer.append( PAGE_LIST );
        t_buffer.append( "?" );
        t_buffer.append( KEY_ID );
        t_buffer.append( "=" );
        t_buffer.append( listID );
        
        // send request
        Thread t_getThread = new Thread() {
            public void run() {
                try {
                    httpGet = new HttpGet( t_buffer.toString() );
                    httpResponse = httpClient.execute( httpGet );
                    statusLine = httpResponse.getStatusLine();
                    statusCode = statusLine.getStatusCode();
                    
                    if ( statusCode == 200 ) {
                        httpEntity = httpResponse.getEntity();
                        InputStream content = httpEntity.getContent();
                        BufferedReader reader = new BufferedReader( new InputStreamReader( content ));
                        String t_line = reader.readLine();
                        t_buffer.setLength( 0 );
                        while ( t_line != null ) {
                            Log.i("GET_LIST", t_line );
                            t_buffer.append( t_line );
                            t_line = reader.readLine();
                        }
                    }
                }
                catch ( ClientProtocolException cpe ) {
                    cpe.printStackTrace();
                }
                catch ( IOException ioe ) {
                    ioe.printStackTrace();
                }
            }
        };
        
        t_getThread.start();
        try {
            t_getThread.join();
        }
        catch ( InterruptedException ie ) {
            ie.printStackTrace();
        }
        int t_breakpoint = 0;
    }
    
    
    
    public void createList( String name ) {
        //POST ./lists.php?name=<name>
        final StringBuffer t_buffer = new StringBuffer();
        t_buffer.append( BASEURL );
        t_buffer.append( PAGE_LIST );
        t_buffer.append( "?" );
        t_buffer.append( KEY_NAME );
        t_buffer.append( "=" );
        t_buffer.append( name );
        
        Thread t_postThread = new Thread() {
            public void run() {
                try {
                    httpPost = new HttpPost( t_buffer.toString() );
                    httpResponse = httpClient.execute( httpPost );
                    statusLine = httpResponse.getStatusLine();
                    statusCode = statusLine.getStatusCode();
                    
                    if ( statusCode == 200 ) {
                        httpEntity = httpResponse.getEntity();
                        InputStream content = httpEntity.getContent();
                        BufferedReader reader = new BufferedReader( new InputStreamReader( content ));
                        String t_line = reader.readLine();
                        t_buffer.setLength( 0 );
                        while ( t_line != null ) {
                            Log.i("CREATE_LIST", t_line );
                            t_buffer.append( t_line );
                            t_line = reader.readLine();
                        }
                    }
                }
                catch ( ClientProtocolException cpe ) {
                    cpe.printStackTrace();
                }
                catch ( IOException ioe ) {
                    ioe.printStackTrace();
                }
            }
        };
        
        t_postThread.start();
        try {
            t_postThread.join();
        }
        catch ( InterruptedException ie ) {
            ie.printStackTrace();
        }
        
        int t_breakpoint = 0;
    }
    
    
    
    // Method to change the name of a list referenced by id, 
    // and renamed to 'name'
    // NOTE: 
    //   I'm getting status code of 400, and have not been
    //   able to determine why.
    public boolean updateList( int listID, String name ) {
        boolean rValue = false;        
        // PUT ./lists.php?id=<listID>&name=<name>
        final StringBuffer t_buffer = new StringBuffer();
        t_buffer.append( BASEURL );
        t_buffer.append( PAGE_LIST );
        
        httpParams = new BasicHttpParams();
        httpParams.setParameter( KEY_NAME, name );
        httpParams.setParameter( KEY_ID, listID );
        
        // send request
        Thread t_putThread = new Thread() {
            public void run() {
                try {
                    httpPut = new HttpPut( t_buffer.toString() );
                    httpPut.setParams( httpParams );
                    httpResponse = httpClient.execute( httpPut );
                    statusLine = httpResponse.getStatusLine();
                    statusCode = statusLine.getStatusCode();
                    
                    if ( statusCode == 200 ) {
                        httpEntity = httpResponse.getEntity();
                        InputStream content = httpEntity.getContent();
                        BufferedReader reader = new BufferedReader( new InputStreamReader( content ));
                        String t_line = reader.readLine();
                        t_buffer.setLength( 0 );
                        while ( t_line != null ) {
                            Log.i("PUT_LIST", t_line );
                            t_buffer.append( t_line );
                            t_line = reader.readLine();
                        }
                    }
                }
                catch ( ClientProtocolException cpe ) {
                    cpe.printStackTrace();
                }
                catch ( IOException ioe ) {
                    ioe.printStackTrace();
                }
            }
        };
        
        t_putThread.start();
        try {
            t_putThread.join();
        }
        catch ( InterruptedException ie ) {
            ie.printStackTrace();
        }
        
        if ( statusCode == 200 ) rValue = true;
        return rValue;
    }
    
    
    
    public boolean deleteList( int listID ) {
        boolean rValue = false;
        // DELETE ./lists.php?id=<id>
        final StringBuffer t_buffer = new StringBuffer();
        t_buffer.append( BASEURL );
        t_buffer.append( PAGE_LIST );
        t_buffer.append( "?" );
        t_buffer.append( KEY_ID );
        t_buffer.append( "=" );
        t_buffer.append( listID );
        
        // send request
        //DeleteThread t_deleteThread = new DeleteThread( t_buffer );
        Thread t_deleteThread = new Thread() {
            public void run() {
                try {
                    httpDelete = new HttpDelete( t_buffer.toString() );
                    httpResponse = httpClient.execute( httpDelete );
                    statusLine = httpResponse.getStatusLine();
                    statusCode = statusLine.getStatusCode();
                    
                    if ( statusCode == 200 ) {
                        httpEntity = httpResponse.getEntity();
                        InputStream content = httpEntity.getContent();
                        BufferedReader reader = new BufferedReader( new InputStreamReader( content ));
                        String t_line = reader.readLine();
                        t_buffer.setLength( 0 );
                        while ( t_line != null ) {
                            t_buffer.append( t_line );
                            Log.i("DELETE_LIST", t_line );
                            t_line = reader.readLine();
                        }
                    }
                }
                catch ( ClientProtocolException cpe ) {
                    cpe.printStackTrace();
                }
                catch ( IOException ioe ) {
                    ioe.printStackTrace();
                }
            }
        };
        t_deleteThread.start();
        try {
            t_deleteThread.join();
        }
        catch ( InterruptedException ie ) {
            ie.printStackTrace();
        }
        
        if ( statusCode == 200 ) rValue = true;
        return rValue;
    }
    
    
    
    // NOTE methods ----------------
    
    public void getListNotes( int listID ) {
        // GET ./list_items.php?list_id=<id>
        final StringBuffer t_buffer = new StringBuffer();
        t_buffer.append( BASEURL );
        t_buffer.append( PAGE_ITEMS );
        t_buffer.append( "?" );
        t_buffer.append( KEY_ID );
        t_buffer.append( "=" );
        t_buffer.append( listID );
        
        // send request
        Thread t_getThread = new Thread() {
            public void run() {
                try {
                    httpGet = new HttpGet( t_buffer.toString() );
                    httpResponse = httpClient.execute( httpGet );
                    statusLine = httpResponse.getStatusLine();
                    statusCode = statusLine.getStatusCode();
                    
                    if ( statusCode == 200 ) {
                        httpEntity = httpResponse.getEntity();
                        InputStream content = httpEntity.getContent();
                        BufferedReader reader = new BufferedReader( new InputStreamReader( content ));
                        String t_line = reader.readLine();
                        t_buffer.setLength( 0 );
                        while ( t_line != null ) {
                            Log.i("GET_LIST", t_line );
                            t_buffer.append( t_line );
                            t_line = reader.readLine();
                        }
                    }
                }
                catch ( ClientProtocolException cpe ) {
                    cpe.printStackTrace();
                }
                catch ( IOException ioe ) {
                    ioe.printStackTrace();
                }
            }
        };
        
        t_getThread.start();
        try {
            t_getThread.join();
        }
        catch ( InterruptedException ie ) {
            ie.printStackTrace();
        }
        int t_breakpoint = 0;
    }
    
    
    
    public void getNote( int noteID ) {
        // GET ./list_items.php?id=<id>
        int t_breakpoint = 0;
    }
    
    
    
    public void createNote( int listID, String noteText ) {
        // POST ./list_items.php?list_id<int>&note=<string>
        final StringBuffer t_buffer = new StringBuffer();
        t_buffer.append( BASEURL );
        t_buffer.append( PAGE_ITEMS );
        //t_buffer.append( "?" );
        //t_buffer.append( KEY_LISTID );
        //t_buffer.append( "=" );
        //t_buffer.append( listID );
        //t_buffer.append( "&" );
        //t_buffer.append( KEY_NOTE );
        //t_buffer.append( "=" );
        //t_buffer.append( noteText );
        httpParams = new BasicHttpParams();
        httpParams.setParameter( KEY_LISTID, listID );
        httpParams.setParameter( KEY_NOTE, noteText );
        
        Thread t_postThread = new Thread() {
            public void run() {
                try {
                    httpPost = new HttpPost( t_buffer.toString() );
                    httpPost.setParams( httpParams );
                    httpResponse = httpClient.execute( httpPost );
                    statusLine = httpResponse.getStatusLine();
                    statusCode = statusLine.getStatusCode();
                    
                    if ( statusCode == 200 ) {
                        httpEntity = httpResponse.getEntity();
                        InputStream content = httpEntity.getContent();
                        BufferedReader reader = new BufferedReader( new InputStreamReader( content ));
                        String t_line = reader.readLine();
                        t_buffer.setLength( 0 );
                        while ( t_line != null ) {
                            Log.i("CREATE_LIST", t_line );
                            t_buffer.append( t_line );
                            t_line = reader.readLine();
                        }
                    }
                }
                catch ( ClientProtocolException cpe ) {
                    cpe.printStackTrace();
                }
                catch ( IOException ioe ) {
                    ioe.printStackTrace();
                }
            }
        };
        
        t_postThread.start();
        try {
            t_postThread.join();
        }
        catch ( InterruptedException ie ) {
            ie.printStackTrace();
        }
        int t_breakpoint = 0;
    }
    
    
    
    public void updateNote( int noteID, String noteText ) {
        // PUT ./list_items.php?name=<string>&id=<int>
        int t_breakpoint = 0;
    }
    
    
    
    public void deleteNote( int noteID ) {
        // DELETE ./list_items.php?id=<id>
        int t_breakpoint = 0;
    }
    
    
    
    
}
