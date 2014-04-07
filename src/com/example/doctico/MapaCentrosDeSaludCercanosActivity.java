package com.example.doctico;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MapaCentrosDeSaludCercanosActivity extends Activity implements OnMarkerClickListener{
		  
	  private GoogleMap map;
	  private LatLng ubicacion_usuario;
	  Location locacion_usuario;

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_mapa_centros_de_salud_cercanos);
	    
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();       // Obtener Mapa
	    
	    if (map != null)
	    {	
	    	cambiarPropiedadesMapa();
	    	colocarMiPoscision();
	    	getLocacionUsuario();
	    	
	  	    LatLng CasaCartago = new LatLng(9.841994,-83.896799);
		    LatLng TEC2 = new LatLng(9.850808,-83.915786 );   
		    LatLng TEC = new LatLng(9.855924,-83.911967);    
	    	agregarMarcador(TEC, "Hospital 1");
	    	agregarMarcador(TEC2, "Hospital 2");
	    	agregarMarcador(CasaCartago, "Hospital 3");
	    }
	  } 

	@Override
	public boolean onMarkerClick(Marker marker) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(marker.getSnippet())
    	       .setTitle(marker.getTitle())
    	       .setNegativeButton("OK", null) 
    	
		       .setPositiveButton("Twittear", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		                // Evento del boton twittear
		            	twittear();
		            }
		        });
    	
    	AlertDialog dialog = builder.create();
		dialog.show();
		return false;
	}
	
	
	private void twittear()
	{
		String tweetUrl = "https://twitter.com/intent/tweet?text=Estoy usando la App DocTico doctico.herokuapp.com";
		Uri uri = Uri.parse(tweetUrl);
		startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}
	
	
    private void agregarMarcador(LatLng centroSalud, String nombreCentro){
	    double distance = getDistanciaLocacionUsuarioTo(centroSalud);
	    int kilometros = (int) distance/1000;
	    int metros = (int) distance - kilometros*1000;
	    
    	 map.addMarker(new MarkerOptions().position(centroSalud)
   		          .title(nombreCentro)
   		          .snippet("Distancia: " + kilometros + " kms y " + metros + " mts")
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
	} 
	
	
	private void cambiarPropiedadesMapa(){
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);       // Cambiar el tipo de mapa a normal
	    map.setMyLocationEnabled(true);                 // Coloca el boton que me lleva a mi posicion actual   
	    map.setOnMarkerClickListener(this);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mapa_centros_de_salud_cercanos, menu);
		return true;
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		if (id == R.id.tipo_mapa_normal) {
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL); 
			return true;
		}
		else if (id == R.id.tipo_mapa_satelite) {
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE); 
			return true;
		}
		else if (id == R.id.tipo_mapa_hibrido) {
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID); 
			return true;
		}
		else if (id == R.id.twitter) {
			twittear(); 
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}