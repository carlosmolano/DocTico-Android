package com.example.doctico;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.doctico.AccesoDatos.JSONParser;
import com.example.doctico.Ayudas.Dialogo;
import com.example.doctico.Ayudas.Estado;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;

public class MapaCentrosDeSaludCercanosActivity extends Activity implements OnMarkerClickListener{
		  
	  private GoogleMap map;
	  private LatLng ubicacion_usuario;
	  private Location locacion_usuario;
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
	    setContentView(R.layout.activity_mapa_centros_de_salud_cercanos);
	    
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
	    token = getIntent().getExtras().getString("Token");
	    estado = new Estado();
	    dialogo = new Dialogo();
	    
	    lista_nombres = getIntent().getExtras().getStringArrayList("Lista_Nombres");
	    lista_latitudes = getIntent().getExtras().getStringArrayList("Lista_Latitudes");
	    lista_longitudes = getIntent().getExtras().getStringArrayList("Lista_Longitudes");
	    lista_mensajes = getIntent().getExtras().getStringArrayList("Lista_Mensajes");
	    
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();       // Obtener Mapa
	    
	    if (map != null){	
	    	cambiarPropiedadesMapa();
	    	colocarMiPoscision();
	    	getLocacionUsuario();
	    	cargarCentros();
	    }
	    
        progress = new ProgressDialog(this);
        progress.setTitle("Por favor espere!!");
        progress.setMessage("Cargando Datos....");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	  } 
	  
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mapa_centros_de_salud_cercanos, menu);
		return true;
	}
	  
	
	@Override
	public boolean onMarkerClick(Marker marker) {	
		if(!marker.getTitle().equalsIgnoreCase("Mi Posición!")){
			String msj_twittear = "Acabo de encontrar el centro de salud " + '"' + marker.getTitle() + '"' + " utilizando la aplicacion DocTico! Visita doctico.herokuapp.com";		
			mostrarDialogoTwittear(marker.getTitle(), marker.getSnippet(), msj_twittear);
		}
		return false;
	}
	
	
	private void cargarCentros(){
	        for (int i = 0; i < lista_nombres.size(); i++) {
		            agregarMarcador(new LatLng(Double.parseDouble(lista_longitudes.get(i)), Double.parseDouble(lista_latitudes.get(i))), 
		            		lista_nombres.get(i), lista_mensajes.get(i));                
		    }
	}
	
	/*
	private void cargarCentros(){
		JSONParser jParser = new JSONParser();
        JSONArray json = jParser.getJSONFromUrl("http://doctico.herokuapp.com/api/api_doc_tico/centros_salud.json?token=" + token);         // get JSON data from URL
        String nombre;
        Double latitud;
        Double longitud;
        String tipo;
        String horario;
        String descripcion;
        String mensaje;
        JSONObject centro_actual;
        
        if(json.length() > 0){
	        for (int i = 0; i < json.length(); i++) {
		        try {
		            centro_actual = json.getJSONObject(i);	            
		            nombre = centro_actual.get("nombre").toString();
		            latitud = Double.parseDouble(centro_actual.get("latitud").toString());
		            longitud = Double.parseDouble(centro_actual.get("longitud").toString());
		            tipo = centro_actual.get("tipo").toString();
		            horario = centro_actual.get("horario").toString();
		            descripcion = centro_actual.get("descripcion").toString();
		            mensaje = "Tipo Centro: " + tipo + "\nHorario: " + horario + "\n" + descripcion; 
		            
		            agregarMarcador(new LatLng(longitud, latitud), nombre, mensaje);                
		        }
		        catch (JSONException e) {
		            e.printStackTrace();
		        }
		    }
        }
	}
	*/
	
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
	
	
    private void agregarMarcador(LatLng centroSalud, String nombreCentro, String info){
	    double distance = getDistanciaLocacionUsuarioTo(centroSalud);
	    int kilometros = (int) distance/1000;
	    int metros = (int) distance - kilometros*1000;
	    
    	map.addMarker(new MarkerOptions().position(centroSalud).title(nombreCentro)
   		          .snippet("Distancia: " + kilometros + " kms y " + metros + " mts\n" + info)
   		          .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital)));
    }
    
    
    private double getDistanciaLocacionUsuarioTo(LatLng centroSalud){
	    Location locationCentroSalud = new Location("Variable locacion");
	    locationCentroSalud.setLatitude(centroSalud.latitude);
	    locationCentroSalud.setLongitude(centroSalud.longitude);
	    return locacion_usuario.distanceTo(locationCentroSalud);
    }
    
	
    private void getLocacionUsuario(){
	    locacion_usuario = new Location("Locacion del Usuario");
	    locacion_usuario.setLatitude(ubicacion_usuario.latitude);
	    locacion_usuario.setLongitude(ubicacion_usuario.longitude);
    }
	
	
	private void colocarMiPoscision(){	  
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    String provider = service.getBestProvider(criteria, false);
	    Location location = service.getLastKnownLocation(provider);
	    ubicacion_usuario = new LatLng(location.getLatitude(),location.getLongitude());    
   	    map.addMarker(new MarkerOptions().position(ubicacion_usuario).title("Mi Posición!")
   	    		.icon(BitmapDescriptorFactory.fromResource(R.drawable.persona)));
   	    
   	    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13.0f));
	} 
	
	
	private void cambiarPropiedadesMapa(){
		cambiarTipoMapa(GoogleMap.MAP_TYPE_NORMAL);
	    map.setMyLocationEnabled(true);                 // Coloca el boton que me lleva a mi posicion actual   
	    map.setOnMarkerClickListener(this);
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		if (id == R.id.tipo_mapa_normal) {
			cambiarTipoMapa(GoogleMap.MAP_TYPE_NORMAL);
			return true;
		}
		else if (id == R.id.tipo_mapa_satelite) {
			cambiarTipoMapa(GoogleMap.MAP_TYPE_SATELLITE);
			return true;
		}
		else if (id == R.id.tipo_mapa_hibrido) {
			cambiarTipoMapa(GoogleMap.MAP_TYPE_HYBRID);
			return true;
		}
		else if (id == R.id.compartir) {
	        String msj_twittear = "Estoy usando la Aplicacion DocTico para encontrar los centros de salud cercanos a mi posicion! Visita doctico.herokuapp.com";		
			mostrarDialogoTwittear("Recomendar DocTico en Twitter", "By Jorge Chavarria Rodriguez\njorge13mtb@gmail.com", msj_twittear);
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
		else if (id == R.id.citas){
	    	progress.show();
    	    new Thread(){
                public void run(){
                	VentanaCitas();
                }
            }.start();
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
    
    
	private void VentanaPresion(){
    	Intent i = new Intent(this, ControlPresionArterialActivity.class);
		i.putExtra("Token", token);
		i.putStringArrayListExtra("Lista_Muestras", obtener_muestras_presion_arterial(token));
    	startActivity(i);
    	progress.dismiss();
    }
	
	
	private void cambiarTipoMapa(int tipo_mapa){
		map.setMapType(tipo_mapa); 
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
	
	
    private void siguientActivity(Class siguienteActivity, String token){
    	Intent i = new Intent(this, siguienteActivity);
		i.putExtra("Token", token);
    	startActivity(i);
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
}