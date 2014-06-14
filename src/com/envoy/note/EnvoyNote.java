package com.envoy.note;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



public class EnvoyNote extends Activity // ActionBarActivity
{

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.notelist_activity_layout );
	}
}
