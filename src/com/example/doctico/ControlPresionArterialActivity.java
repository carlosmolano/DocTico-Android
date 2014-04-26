package com.example.doctico;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.doctico.AccesoDatos.JSONParser;
import com.example.doctico.Ayudas.Dialogo;
import com.example.doctico.Ayudas.Estado;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ControlPresionArterialActivity extends Activity {
	
	private ListView lista_muestras_presion;
    private String token;
    private Estado estado;
    private Dialogo dialogo;
    private int cantidad_muestras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_presion_arterial);
		
		lista_muestras_presion = (ListView) findViewById(R.id.lista_muestras_presion);
	    token = getIntent().getExtras().getString("Token");
	    estado = new Estado();
	    dialogo = new Dialogo();
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
			siguientActivity(AgregarMuestraPresionArterialActivity.class, token);
			return true;
		}
		else if(id == R.id.graficar_muestras)
		{
			if(cantidad_muestras>1)
				siguientActivity(GraficoPresionArterialActivity.class, token);
			else
				mostrarDialogo("Upps...", "Deben de existir al menos 2 muestras para generar el grafico");
			return true;
		}
		else if (id == R.id.mapa){
			siguientActivity(MapaCentrosDeSaludCercanosActivity.class, token);
			return true;
		}
		else if (id == R.id.citas){
			siguientActivity(ControlCitasActivity.class, token);
			return true;
		}
		else if (id == R.id.recomendar_doctico) {
	        String msj_twittear = "Estoy usando la Aplicacion DocTico para encontrar los centros de salud cercanos a mi posicion! Visita doctico.herokuapp.com";		
			mostrarDialogoTwittear("Recomendar DocTico en Twitter", "By Jorge Chavarria Rodriguez\njorge13mtb@gmail.com", msj_twittear);
			return true;
		}
		else if (id == R.id.cerrar_sesion){
			siguientActivity(IniciarSesionActivity.class, token);
			this.finish();
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
	
	
	private void twittear(String mensaje){
		String tweetUrl = "https://twitter.com/intent/tweet?text=" + mensaje;
		Uri uri = Uri.parse(tweetUrl);
		startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}
	
	
	private void mostrarDialogoTwittear(String titulo, String mensaje, final String mensaje_twitter)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(mensaje)
    	       .setTitle(titulo)
    	       .setNegativeButton("OK", null) 
		       .setPositiveButton("Twittear", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		    			if(estado.ConexionInternetDisponible((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)))
		    				twittear(mensaje_twitter);
		    			else
		    				mostrarMensajeErrorConexionInternet();    
		        }});
    	AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	
	private void mostrarMensajeErrorConexionInternet(){
		dialogo.mostrar("Internet", "Se requiere Internet para completar esta transaccion!", this);
	}
	
	
	private void mostrarDialogo(String titulo, String mensaje){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(mensaje).setTitle(titulo).setNegativeButton("OK", null);
    	AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	
    private void siguientActivity(Class siguienteActivity, String token){
    	Intent i = new Intent(this, siguienteActivity);
		i.putExtra("Token", token);
    	startActivity(i);
    }
}