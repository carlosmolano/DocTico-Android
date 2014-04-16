package com.example.doctico;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_funcionalidades);

	    token = getIntent().getExtras().getString("Token");
	    estado = new Estado();
	    dialogo = new Dialogo();
		
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
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.cerrar_sesion) {
		      Intent intent = new Intent(this, IniciarSesionActivity.class);
		      
  			  JSONParser jsonparser = new JSONParser();
  			  jsonparser.cerrar_sesion(token);
		      
		      this.finish();
		      startActivity(intent);
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
	    		    if(estado.GpsDisponible((LocationManager) getSystemService(Context.LOCATION_SERVICE)))
	    		    	siguientActivity(MapaCentrosDeSaludCercanosActivity.class, token);	
	    		    else 
	    		    	errorGPS();
	    		}
	    		
	    		else if(v.getId() == boton_to_control_presion.getId())
	    			siguientActivity(ControlPresionArterialActivity.class, token);
	    		
	    		else if(v.getId() == boton_to_control_citas.getId())
	    			siguientActivity(ControlCitasActivity.class, token);
    		}
    		else
    			errorConexionInternet();
    	}
    };
    
    
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