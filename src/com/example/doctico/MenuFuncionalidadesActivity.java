package com.example.doctico;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuFuncionalidadesActivity extends Activity {
	
	private Button boton_to_centros_ccss;
	private Button boton_to_centros_privados;
	private Button boton_to_control_presion;
	private Button boton_to_control_citas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_funcionalidades);
		
		boton_to_centros_ccss = (Button)findViewById(R.id.btn_to_centros_de_salud_ccss);
		boton_to_centros_ccss.setOnClickListener(Eventos_Botones);    
		
		boton_to_centros_privados = (Button)findViewById(R.id.btn_to_centros_de_salud_privados);
		boton_to_centros_privados.setOnClickListener(Eventos_Botones);    
		
		boton_to_control_presion = (Button)findViewById(R.id.btn_to_control_presion);
		boton_to_control_presion.setOnClickListener(Eventos_Botones);
		
		boton_to_control_citas = (Button)findViewById(R.id.btn_to_control_citas);
		boton_to_control_citas.setOnClickListener(Eventos_Botones);
	}
	
	
    private OnClickListener Eventos_Botones = new OnClickListener()    // Metodo de evento de botones
    {
    	public void onClick(final View v)
    	{  	
    		if(v.getId() == boton_to_centros_ccss.getId() || v.getId() == boton_to_centros_privados.getId()) 
    		{
    		    if(estadoGPS() == true)
    		    	ventanaMapaCentros(v);
    		    else
    		    	mostrarMensajeErrorGPS();
    		}
    		
    		else if(v.getId() == boton_to_control_presion.getId())
    			ventanaControlPresionArterial(v);
    		
    		else if(v.getId() == boton_to_control_citas.getId())
    			ventanaControlCitas(v);
    	}
    };
	
    
    private boolean estadoGPS()
    {
	    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
	    boolean estadoGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	return estadoGPS;
    }

    private void mostrarMensajeErrorGPS()
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Se requiere el uso del GPS, por favor active el GPS!")
    	       .setTitle("GPS")
    	       .setPositiveButton("OK", null);
    	AlertDialog dialog = builder.create();
		dialog.show();
    }
    

    public void ventanaMapaCentros(View view){
		Intent i = new Intent(this, MapaCentrosDeSaludCercanosActivity.class);
		startActivity(i);
    }
    
    
    public void ventanaControlCitas(View view){
		Intent i = new Intent(this, ControlCitasActivity.class);
		startActivity(i);
    }
    
    
    public void ventanaControlPresionArterial(View view){
		Intent i = new Intent(this, ControlPresionArterialActivity.class);
		startActivity(i);
    }
}