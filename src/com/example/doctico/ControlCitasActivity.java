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

public class ControlCitasActivity extends Activity {
	
	private ListView lista_citas;
	private String token;
	private Dialogo dialogo;
	private Estado estado;
    private int cantidad_citas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_citas);
		
		lista_citas = (ListView) findViewById(R.id.lista_citas);
		dialogo = new Dialogo();
		estado = new Estado(); 
	    token = getIntent().getExtras().getString("Token");
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
			siguientActivity(AgregarCitaActivity.class, token);
			return true;
		}
		else if (id == R.id.mapa){
			siguientActivity(MapaCentrosDeSaludCercanosActivity.class, token);
			return true;
		}
		else if (id == R.id.presion_arterial){
			siguientActivity(ControlPresionArterialActivity.class, token);
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
		        	lista_muestras.add(identificador  + "\n  A las " +hora + " del dia " + fecha 
		        			                          + "\n  En " + centro);
		        }
		        catch (JSONException e) {
		            e.printStackTrace();
		        }
		    }
	    }
	    else
	    	noHayCitas();
	    
	    Collections.reverse(lista_muestras);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, lista_muestras);
        lista_citas.setAdapter(adapter); 
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
