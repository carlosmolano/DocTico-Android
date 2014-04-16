package com.example.doctico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ControlCitasActivity extends Activity {
	
	private String token;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_citas);
		
	    Bundle bundle = getIntent().getExtras();
	    token = bundle.getString("Token");
	    System.out.println(bundle.getString("Token"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.control_citas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.agregar_cita) {
		    Intent intent = new Intent(this, AgregarCitaActivity.class);
		    intent.putExtra("Token", token);
		    this.finish();
		    startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
