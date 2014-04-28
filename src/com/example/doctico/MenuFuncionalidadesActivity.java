package com.example.doctico;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.doctico.AccesoDatos.JSONParser;
import com.example.doctico.Ayudas.Dialogo;
import com.example.doctico.Ayudas.Estado;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuFuncionalidadesActivity extends Activity {
	
	private Button boton_to_centros_salud;
	private Button boton_to_control_presion;
	private Button boton_to_control_citas;
	private String token;
	private Estado estado;
	private Dialogo dialogo;
	private ProgressDialog progress;
	ArrayList<String> lista_nombres;
	ArrayList<String> lista_latitudes;
	ArrayList<String> lista_longitudes;
	ArrayList<String> lista_mensajes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_funcionalidades);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 

	    token = getIntent().getExtras().getString("Token");
	    estado = new Estado();
	    dialogo = new Dialogo();
	    
        progress = new ProgressDialog(this);
        progress.setTitle("Por favor espere!!");
        progress.setMessage("Cargando Datos....");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		boton_to_centros_salud = (Button)findViewById(R.id.btn_to_centros_de_salud);
		boton_to_centros_salud.setOnClickListener(Eventos_Botones);    
		
		boton_to_control_presion = (Button)findViewById(R.id.btn_to_control_presion);
		boton_to_control_presion.setOnClickListener(Eventos_Botones);
		
		boton_to_control_citas = (Button)findViewById(R.id.btn_to_control_citas);
		boton_to_control_citas.setOnClickListener(Eventos_Botones);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_funcionalidades, menu);
	    return true;	    
	}
	
	private void obtenerCentros(){
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
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.cerrar_sesion) {
		      if(estado.ConexionInternetDisponible((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))){
		    	  JSONParser jsonparser = new JSONParser();
		    	  jsonparser.cerrar_sesion(token);
		      	}
		      Intent intent = new Intent(this, IniciarSesionActivity.class);
		      this.finish();
		      startActivity(intent);
			  return true;
		}
		
		else if (id == R.id.recomendar_doctico){
	        String msj_twittear = "Estoy usando la Aplicacion DocTico para encontrar los centros de salud cercanos a mi posicion! Visita doctico.herokuapp.com";		
			mostrarDialogoTwittear("Recomendar DocTico en Twitter", "By Jorge Chavarria Rodriguez\njorge13mtb@gmail.com", msj_twittear);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
    private OnClickListener Eventos_Botones = new OnClickListener()    // Metodo de evento de botones
    {
    	public void onClick(final View v)
    	{  	
    		if(estado.ConexionInternetDisponible((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)))
    		{
	    		if(v.getId() == boton_to_centros_salud.getId()) 
	    		{
	    		    if(estado.GpsDisponible((LocationManager) getSystemService(Context.LOCATION_SERVICE))){
		   	    	    progress.show();
	    	    	    new Thread(){
	    	                public void run(){
	    	                	VentanaMapa();
	    	                }
	    	            }.start();
	    		    }
	    		    else 
	    		    	errorGPS();
	    		}
	    		
	    		else if(v.getId() == boton_to_control_presion.getId()){
	   	    	    progress.show();
    	    	    new Thread(){
    	                public void run(){
    	                	VentanaPresion();
    	                }
    	            }.start();
	    		}
	    		else if(v.getId() == boton_to_control_citas.getId()){
	   	    	    progress.show();
    	    	    new Thread(){
    	                public void run(){
    		    			VentanaCitas();
    	                }
    	            }.start();
	    		}
    		}
    		else
    			errorConexionInternet();
    	}
    };
    
    
    private void VentanaMapa(){
    	Intent i = new Intent(this, MapaCentrosDeSaludCercanosActivity.class);
		i.putExtra("Token", token);
		obtenerCentros();
		i.putStringArrayListExtra("Lista_Nombres", lista_nombres);
		i.putStringArrayListExtra("Lista_Latitudes", lista_latitudes);
		i.putStringArrayListExtra("Lista_Longitudes", lista_longitudes);
		i.putStringArrayListExtra("Lista_Mensajes", lista_mensajes);
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
    
    
    private void VentanaCitas(){
    	Intent i = new Intent(this, ControlCitasActivity.class);
		i.putExtra("Token", token);
		i.putStringArrayListExtra("Lista_Citas", obtener_citas(token));
    	startActivity(i);
    	progress.dismiss();
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
		    				errorConexionInternet();    
		        }});
    	AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	
	private void twittear(String mensaje){
		String tweetUrl = "https://twitter.com/intent/tweet?text=" + mensaje;
		Uri uri = Uri.parse(tweetUrl);
		startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}
    
    
	private void errorGPS(){
		dialogo.mostrar("GPS", "Se requiere el uso del GPS, por favor active el GPS!", this);
	}
	
	
	private void errorConexionInternet(){
		dialogo.mostrar("Internet", "Se requiere Internet para completar esta transaccion!", this);
	}	
    
    
    private void siguientActivity(Class siguienteActivity, String token){
    	Intent i = new Intent(this, siguienteActivity);
		i.putExtra("Token", token);
    	startActivity(i);
    }
}