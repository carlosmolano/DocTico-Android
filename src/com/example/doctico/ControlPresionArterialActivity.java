package com.example.doctico;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ControlPresionArterialActivity extends Activity {
	
    private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_presion_arterial);
		
	    Bundle bundle = getIntent().getExtras();
	    token = bundle.getString("Token");
	    System.out.println(bundle.getString("Token"));
	    
	    obtener_muestras_presion_arterial(token);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.control_presion_arterial, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.agregar_control_presion) {
			Intent intent = new Intent(this, AgregarMuestraPresionArterialActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void obtener_muestras_presion_arterial(String token)
	{
		JSONParser jParser = new JSONParser();
	    JSONArray json = jParser.getJSONFromUrl("http://doctico.herokuapp.com/api/api_doc_tico/presion_arterial.json?token=" + token);         // get JSON data from URL
	    String hora;
	    String fecha;
	    String sistolica;
	    String diastolica;
	    JSONObject muestra_actual;
	    
	    if(json.length() > 0){
	        for (int i = 0; i < json.length(); i++) {
		        try {
		        	muestra_actual = json.getJSONObject(i);	            
		        	hora = muestra_actual.get("hora").toString();
		        	fecha = muestra_actual.get("fecha").toString();
		        	sistolica = muestra_actual.get("sistolica").toString();
		        	diastolica = muestra_actual.get("diastolica").toString();
		            System.out.println(hora);
		            System.out.println(fecha);
		            System.out.println(sistolica);
		            System.out.println(diastolica);
		        }
		        catch (JSONException e) {
		            e.printStackTrace();
		        }
		    }
	    }
	    else
	    	System.out.println("No tiene muestas de presion arterial....");
	}
}
