package com.xury.vicluanch;

import com.xury.vicluanch.menu.view.BaseLunchFlipper;
import com.xury.vicluanch.menu.view.SeeyonMainMenuLayout;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		BaseLunchFlipper lun=(BaseLunchFlipper)findViewById(R.id.blun);
		SeeyonMainMenuLayout l=new SeeyonMainMenuLayout(this);
		l.setLayout(lun);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
