package com.tetrix.bluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Help extends Activity {
	public TextView content;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		content = (TextView) findViewById(R.id.help_content);
		content.setEditableFactory(null);
		//content.setText(R.string.help_content);
	}
}