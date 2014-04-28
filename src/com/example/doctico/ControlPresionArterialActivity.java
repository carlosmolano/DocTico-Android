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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ControlPresionArterialActivity extends Activity {
	
	private ListView lista_muestras_presion;
    private String token;
    private Estado estado;
    private Dialogo dialogo;
	private ProgressDialog progress;
	
    ArrayList<String> lista_muestras;
    ArrayList<String> diastolicas;
    ArrayList<String> sistolicas;
    ArrayList<String> fechas;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_presion_arterial);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		lista_muestras_presion = (ListView) findViewById(R.id.lista_muestras_presion);
	    token = getIntent().getExtras().getString("Token");
	    lista_muestras = getIntent().getExtras().getStringArrayList("Lista_Muestras");
	    diastolicas = new ArrayList<String>();
	    sistolicas = new ArrayList<String>();
	    fechas = new ArrayList<String>();
	    
        progress = new ProgressDialog(this);
        progress.setTitle("Por favor espere!!");
        progress.setMessage("Cargando Datos....");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    
	    mostrar_muestras_presion_arterial(lista_muestras);
	    estado = new Estado();
	    dialogo = new Dialogo();
	}
	
	
	private void obtener_sistolicas_diastolicas(){
		JSONParser jParser = new JSONParser();
	    JSONArray json = jParser.getJSONFromUrl("http://doctico.herokuapp.com/api/api_doc_tico/presion_arterial.json?token=" + token);
	    JSONObject muestra_actual;
	         		
        int cantidad_muestras = json.length();
        
	    if(cantidad_muestras > 0){
	        for (int i = 0; i < cantidad_muestras; i++) {
		        try {
		        	muestra_actual = json.getJSONObject(i);	         
		        	sistolicas.add(muestra_actual.get("sistolica").toString());
		        	diastolicas.add(muestra_actual.get("diastolica").toString());
		        	fechas.add(muestra_actual.get("fecha").toString());
		        }
		        catch (JSONException e) {
		            e.printStackTrace();
		        }
		    }
	    }
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
			if(lista_muestras.size()>2){
		    	progress.show();
	    	    new Thread(){
	                public void run(){
	                	VentanaGrafico();
	                }
	            }.start();
			}
			else
				mostrarDialogo("Upps...", "Deben de existir al menos 2 muestras para generar el grafico");
			return true;
		}
		else if (id == R.id.mapa){
			siguientActivity(MapaCentrosDeSaludCercanosActivity.class, token);
			return true;
		}
		else if (id == R.id.citas){
	    	progress.show();
    	    new Thread(){
                public void run(){
                	VentanaCitas();
                }
            }.start();
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
	
	
    private void VentanaCitas(){
    	Intent i = new Intent(this, ControlCitasActivity.class);
		i.putExtra("Token", token);
		i.putStringArrayListExtra("Lista_Citas", obtener_citas(token));
    	startActivity(i);
    	progress.dismiss();
    }
	
    private void VentanaGrafico(){
    	obtener_sistolicas_diastolicas();
    	Intent i = new Intent(this, GraficoPresionArterialActivity.class);
		i.putStringArrayListExtra("Sistolicas", sistolicas);
		i.putStringArrayListExtra("Diastolicas", diastolicas);
		i.putStringArrayListExtra("Fechas", fechas);
    	startActivity(i);
    	progress.dismiss();
    }
	
	private void mostrar_muestras_presion_arterial(ArrayList<String> lista_muestras)
	{
        lista_muestras.add("     Sis.            Dia.          Fecha");
	    Collections.reverse(lista_muestras);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, lista_muestras);
        lista_muestras_presion.setAdapter(adapter);
        
        if(lista_muestras.size() == 1)
        	mostrarDialogo("Hora de Iniciar :)", "Utilizar la opcion 'NUEVA MUESTRA' para iniciar tu control de Presion Arterial");
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
	
    
	private ArrayList<String> obtener_citas(String token)
	{
		JSONParser jParser = new JSONParser();
	    JSONArray json = jParser.getJSONFromUrl("http://doctico.herokuapp.com/api/api_doc_tico/citas.json?token=" + token);         // get JSON data from URL
	   
	    String identificador;
	    String hora;
	    String fecha;
	    String centro; 
	    JSONObject cita_actual;
	    
        ArrayList<String> lista_muestras = new ArrayList<String>();       		
        int cantidad_citas = json.length();
        
        for (int i = 0; i < cantidad_citas; i++) {
	        try {
	        	cita_actual = json.getJSONObject(i);	 
	        	identificador = cita_actual.get("identificador").toString();
	        	hora = cita_actual.get("hora").toString();
	        	fecha = cita_actual.get("fecha").toString();
	        	centro = cita_actual.get("centro").toString();
	        	lista_muestras.add(identificador  + "\n  A las " +hora + " del dia " + fecha 
	        			                          + "\n  En " + centro);
	        }
	        catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }
        return lista_muestras;
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