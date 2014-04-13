package com.example.doctico;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_funcionalidades);
		
	    Bundle bundle = getIntent().getExtras();
	    token = bundle.getString("Token");
	    System.out.println(bundle.getString("Token"));
		
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
		      
		      // Aca deberia de cambiar el token del usuario
		      
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
    		if(v.getId() == boton_to_centros_salud.getId()) 
    		{
    		    if(estadoGPS()){
    		    	if(estadoConexionInternet())
    		    		siguientActivity(MapaCentrosDeSaludCercanosActivity.class, token);
    		    	else
    		    		mostrarDialogo("Internet", "Se requiere el uso de Internet!");
    		    }
    		    else 
    		    	mostrarDialogo("GPS", "Se requiere el uso del GPS, por favor active el GPS!");
    		}
    		
    		else if(v.getId() == boton_to_control_presion.getId())
    			siguientActivity(ControlPresionArterialActivity.class);
    		
    		else if(v.getId() == boton_to_control_citas.getId())
    			siguientActivity(ControlCitasActivity.class);
    	}
    };
	
    
    private boolean estadoGPS(){
	    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
	    boolean estadoGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	return estadoGPS;
    }
    
    
	private boolean estadoConexionInternet(){
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	    
    
    private void mostrarDialogo(String titulo, String contenido){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(titulo)
    		   .setMessage(contenido)
    	       .setPositiveButton("OK", null);
    	AlertDialog dialog = builder.create();
		dialog.show();
    }
    
    
    private void siguientActivity(Class siguienteActivity, String token){
    	Intent i = new Intent(this, siguienteActivity);
		i.putExtra("Token", token);
    	startActivity(i);
    }
    
    
    private void siguientActivity(Class siguienteActivity){
    	Intent i = new Intent(this, siguienteActivity);
    	startActivity(i);
    }
}