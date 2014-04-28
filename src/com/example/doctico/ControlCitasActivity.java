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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ControlCitasActivity extends Activity {
	
	private ListView lista_citas;
	private String token;
	private Dialogo dialogo;
	private Estado estado;
    ArrayList<String> lista_cita;
	private ProgressDialog progress;
	
	ArrayList<String> lista_nombres;
	ArrayList<String> lista_latitudes;
	ArrayList<String> lista_longitudes;
	ArrayList<String> lista_mensajes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_citas);
		
		lista_citas = (ListView) findViewById(R.id.lista_citas);
		dialogo = new Dialogo();
		estado = new Estado(); 
	    token = getIntent().getExtras().getString("Token");
	    lista_cita = getIntent().getExtras().getStringArrayList("Lista_Citas");
	    
        progress = new ProgressDialog(this);
        progress.setTitle("Por favor espere!!");
        progress.setMessage("Cargando Datos....");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

	    mostrar_citas(lista_cita);
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
	    	progress.show();
    	    new Thread(){
                public void run(){
                	VentanaAgregarCita();
                }
            }.start();
			return true;
		}
		else if (id == R.id.mapa){
	    	progress.show();
    	    new Thread(){
                public void run(){
                	VentanaMapa();
                }
            }.start();
			return true;
		}
		else if (id == R.id.presion_arterial){
	    	progress.show();
    	    new Thread(){
                public void run(){
                	VentanaPresion();
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
	
	
	private void mostrar_citas(ArrayList<String> lista_muestras)
	{
	    Collections.reverse(lista_muestras);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, lista_muestras);
        lista_citas.setAdapter(adapter); 
        
        if(lista_muestras.size() == 0)
        	noHayCitas();	
	}
	
	
	private ArrayList<String> obtenerCentros(){
		ArrayList<String> lista_centros = new ArrayList<String>();
		lista_centros.add("Centro por Defecto");
		
		JSONParser jParser = new JSONParser();
        JSONArray json = jParser.getJSONFromUrl("http://doctico.herokuapp.com/api/api_doc_tico/nombres_centros_salud.json?token=" + token);         // get JSON data from URL

        for (int i = 0; i < json.length(); i++) {
	        try {            
	            lista_centros.add(json.getJSONObject(i).get("nombre").toString());
	        }
	        catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }
        
		return lista_centros;
	}
	
	
    private void VentanaAgregarCita(){
    	Intent i = new Intent(this, AgregarCitaActivity.class);
		i.putExtra("Token", token);
		i.putStringArrayListExtra("Lista_Centros", obtenerCentros());
    	startActivity(i);
    	progress.dismiss();
    }
	
    
	private void VentanaPresion(){
    	Intent i = new Intent(this, ControlPresionArterialActivity.class);
		i.putExtra("Token", token);
		i.putStringArrayListExtra("Lista_Muestras", obtener_muestras_presion_arterial(token));
    	startActivity(i);
    	progress.dismiss();
    }
	
	
   private void VentanaMapa(){
    	Intent i = new Intent(this, MapaCentrosDeSaludCercanosActivity.class);
		i.putExtra("Token", token);
		obtenerCentrosSalud();
		i.putStringArrayListExtra("Lista_Nombres", lista_nombres);
		i.putStringArrayListExtra("Lista_Latitudes", lista_latitudes);
		i.putStringArrayListExtra("Lista_Longitudes", lista_longitudes);
		i.putStringArrayListExtra("Lista_Mensajes", lista_mensajes);
    	startActivity(i);
    	progress.dismiss();
    }
	
	
	private void obtenerCentrosSalud(){
		JSONParser jParser = new JSONParser();
        JSONArray json = jParser.getJSONFromUrl("http://doctico.herokuapp.com/api/api_doc_tico/centros_salud.json?token=" + token);         // get JSON data from URL
        String nombre;
        String latitud;
        String longitud;
        String tipo;
        String horario;
        String descripcion;
        String mensaje;
        
        lista_nombres = new ArrayList<String>();
        lista_latitudes = new ArrayList<String>();
        lista_longitudes = new ArrayList<String>();
        lista_mensajes = new ArrayList<String>();
        
        JSONObject centro_actual;
        
        if(json.length() > 0){
	        for (int i = 0; i < json.length(); i++) {
		        try {
		            centro_actual = json.getJSONObject(i);	            
		            nombre = centro_actual.get("nombre").toString();
		            latitud = centro_actual.get("latitud").toString();
		            longitud = centro_actual.get("longitud").toString();
		            tipo = centro_actual.get("tipo").toString();
		            horario = centro_actual.get("horario").toString();
		            descripcion = centro_actual.get("descripcion").toString();
		            mensaje = "Tipo Centro: " + tipo + "\nHorario: " + horario + "\n" + descripcion; 
		            
		            lista_nombres.add(nombre);
		            lista_mensajes.add(mensaje);
		            lista_latitudes.add(latitud);
		            lista_longitudes.add(longitud);               
		        }
		        catch (JSONException e) {
		            e.printStackTrace();
		        }
		    }
        }
	}
	
	
	private ArrayList<String> obtener_muestras_presion_arterial(String token)
	{
		JSONParser jParser = new JSONParser();
	    JSONArray json = jParser.getJSONFromUrl("http://doctico.herokuapp.com/api/api_doc_tico/presion_arterial.json?token=" + token);         // get JSON data from URL
	    String fecha;
	    String sistolica;
	    String diastolica;
	    JSONObject muestra_actual;
	    
        ArrayList<String> lista_muestras = new ArrayList<String>();       		
        int cantidad_muestras = json.length();
        
	    if(cantidad_muestras > 0){
	        for (int i = 0; i < cantidad_muestras; i++) {
		        try {
		        	muestra_actual = json.getJSONObject(i);	         
		        	fecha = muestra_actual.get("fecha").toString();
		        	sistolica = muestra_actual.get("sistolica").toString();
		        	diastolica = muestra_actual.get("diastolica").toString();
		        	lista_muestras.add("     " + sistolica + "            " + diastolica + "            " + fecha);
		        }
		        catch (JSONException e) {
		            e.printStackTrace();
		        }
		    }
	    }
	    return lista_muestras;
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
	
	
	private void noHayCitas(){
		dialogo.mostrar("Hora de Iniciar :)", "Utilizar la opcion 'AGRERAR CITA' para iniciar tu control de citas", this);
	}
	
	
    private void siguientActivity(Class siguienteActivity, String token){
    	Intent i = new Intent(this, siguienteActivity);
		i.putExtra("Token", token);
    	startActivity(i);
    }
}
