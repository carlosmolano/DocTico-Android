package com.example.doctico;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ControlCitasActivity extends Activity {
	
	private ListView lista_citas;
	private String token;
    private int cantidad_citas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_citas);
		
		lista_citas = (ListView) findViewById(R.id.lista_citas);
		
	    Bundle bundle = getIntent().getExtras();
	    token = bundle.getString("Token");
	    System.out.println(bundle.getString("Token"));
	    
	    obtener_citas(token);
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
	
	
	private void obtener_citas(String token)
	{
		JSONParser jParser = new JSONParser();
	    JSONArray json = jParser.getJSONFromUrl("http://doctico.herokuapp.com/api/api_doc_tico/citas.json?token=" + token);         // get JSON data from URL
	   
	    String identificador;
	    String hora;
	    String fecha;
	    String centro;
	    
	    JSONObject cita_actual;
	    
        ArrayList<String> lista_muestras = new ArrayList<String>();       		
        cantidad_citas = json.length();
        
	    if(cantidad_citas > 0){
	        for (int i = 0; i < cantidad_citas; i++) {
		        try {
		        	cita_actual = json.getJSONObject(i);	 
		        	identificador = cita_actual.get("identificador").toString();
		        	hora = cita_actual.get("hora").toString();
		        	fecha = cita_actual.get("fecha").toString();
		        	centro = cita_actual.get("centro").toString();
		        	lista_muestras.add(identificador 
		        			           + "\n  A las " +hora + " del dia " + fecha 
		        			           + "\n  En " + centro);
		        }
		        catch (JSONException e) {
		            e.printStackTrace();
		        }
		    }
	    }
	    else
	    	mostrarDialogo("Hora de Iniciar :)", "Utilizar la opcion 'AGRERAR CITA' para iniciar tu control de citas");
	    
	    Collections.reverse(lista_muestras);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, lista_muestras);
        lista_citas.setAdapter(adapter); 
	}
	
	
	private void mostrarDialogo(String titulo, String mensaje){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(mensaje).setTitle(titulo).setNegativeButton("OK", null);
    	AlertDialog dialog = builder.create();
		dialog.show();
	}
}
