package com.example.doctico;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MapaCentrosDeSaludCercanosActivity extends Activity implements OnMarkerClickListener{
		  
	  private GoogleMap map;
	  private LatLng ubicacion_usuario;
	  private Location locacion_usuario;
      private String token;
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_mapa_centros_de_salud_cercanos);
	    
	    token = getIntent().getExtras().getString("Token");
	    
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
	    
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();       // Obtener Mapa
	    
	    if (map != null){	
	    	cambiarPropiedadesMapa();
	    	colocarMiPoscision();
	    	getLocacionUsuario();
	    	cargarCentros();
	    }
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
        else
        	System.out.println("Este mae esta mamando, debemos tirarlo afuera de la aplicacion");
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
	    System.out.println("----55555555");
	    ubicacion_usuario = new LatLng(location.getLatitude(),location.getLongitude());    
	    System.out.println("----66666666");
   	    map.addMarker(new MarkerOptions().position(ubicacion_usuario).title("Mi Posición!")
   	    		.icon(BitmapDescriptorFactory.fromResource(R.drawable.persona)));
	} 
	
	
	private void cambiarPropiedadesMapa(){
		cambiarTipoMapa(GoogleMap.MAP_TYPE_NORMAL);
	    map.setMyLocationEnabled(true);                 // Coloca el boton que me lleva a mi posicion actual   
	    map.setOnMarkerClickListener(this);
	}
	
	
	private boolean estadoConexionInternet(){
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
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
		return super.onOptionsItemSelected(item);
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
		    			if(estadoConexionInternet())
		    				twittear(mensaje_twitter);
		    			else
		    				mostrarMensajeErrorConexionInternet();    
		        }});
    	AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	
	private void mostrarMensajeErrorConexionInternet(){
		Context context = getApplicationContext();
		CharSequence text = "Se requiere conexion a internet";
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}