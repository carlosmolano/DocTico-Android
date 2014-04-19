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

public class ControlPresionArterialActivity extends Activity {
	
	private ListView lista_muestras_presion;
    private String token;
    private int cantidad_muestras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_presion_arterial);
		
		lista_muestras_presion = (ListView) findViewById(R.id.lista_muestras_presion);
	    token = getIntent().getExtras().getString("Token");
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
			intent.putExtra("Token", token);
			this.finish();
			startActivity(intent);
			return true;
		}
		
		else if(id == R.id.graficar_muestras)
		{
			if(cantidad_muestras>1){
				Intent intent = new Intent(this, GraficoPresionArterialActivity.class);
				intent.putExtra("Token", token);
				startActivity(intent);
			}
			else
				mostrarDialogo("Upps...", "Deben de existir al menos 2 muestras para generar el grafico");
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
	    
        ArrayList<String> lista_muestras = new ArrayList<String>();       		
        cantidad_muestras = json.length();
        
	    if(cantidad_muestras > 0){
	        for (int i = 0; i < cantidad_muestras; i++) {
		        try {
		        	muestra_actual = json.getJSONObject(i);	            
		        	hora = muestra_actual.get("hora").toString();
		        	fecha = muestra_actual.get("fecha").toString();
		        	sistolica = muestra_actual.get("sistolica").toString();
		        	diastolica = muestra_actual.get("diastolica").toString();
		        	lista_muestras.add("  " + sistolica + "         " + diastolica + "          " + hora + "     " + fecha);
		        }
		        catch (JSONException e) {
		            e.printStackTrace();
		        }
		    }
	    }
	    else
	    	mostrarDialogo("Hora de Iniciar :)", "Utilizar la opcion 'NUEVA MUESTRA' para iniciar tu control de presion arterial");
	    
        lista_muestras.add("  Sis.        Dia.        Hora     Fecha");
	    Collections.reverse(lista_muestras);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, lista_muestras);
        lista_muestras_presion.setAdapter(adapter); 
	}
	
	
	private void mostrarDialogo(String titulo, String mensaje){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(mensaje).setTitle(titulo).setNegativeButton("OK", null);
    	AlertDialog dialog = builder.create();
		dialog.show();
	}
}