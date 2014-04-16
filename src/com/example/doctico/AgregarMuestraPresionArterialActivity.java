package com.example.doctico;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AgregarMuestraPresionArterialActivity extends Activity {
	
	private EditText miTiempo;
	private EditText miFecha;
	private EditText miSistolica;
	private EditText miDiastolica;
	private String token;
	private Button boton_agregar_muestra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_muestra_presion_arterial);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		token = getIntent().getExtras().getString("Token");
		miTiempo = (EditText) findViewById(R.id.texto_hora);
		miFecha = (EditText) findViewById(R.id.texto_fecha);
		miSistolica = (EditText) findViewById(R.id.texto_sistolica);
		miDiastolica = (EditText) findViewById(R.id.texto_diastolica);
		
		boton_agregar_muestra = (Button)findViewById(R.id.btn_agregar_muestra);      
		boton_agregar_muestra.setOnClickListener(Eventos_Botones);   

		Calendar calendar = Calendar.getInstance();
		miTiempo.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
		miFecha.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" +  calendar.get(Calendar.YEAR));
		
		miSistolica.setText("120");
		miDiastolica.setText("60");
	}
	
		
    private OnClickListener Eventos_Botones = new OnClickListener()    // Metodo de evento de botones
    {
    	public void onClick(final View v)
    	{
    		if(v.getId() == boton_agregar_muestra.getId())            // Evento de Ingresar a la aplicaci�n
    		{
    			JSONParser jsonparser = new JSONParser();
    			String respuesta = jsonparser.agregar_muestra_presion(token, miTiempo.getText().toString(), 
    					           miFecha.getText().toString(), miSistolica.getText().toString(), miDiastolica.getText().toString());		
    			
    			if(respuesta.equals("Si")){
    				mostrarDialogo(":)", "Se ha agregado correctamente tu nueva muestra de presion arterial");  
    				ventanaControlṔresion();
    			}
    			else
    				mostrarDialogo("Upps...", "Ha ocurrido un error y No se pudo agregar la nueva muestra, intentelo otra vez...");   
    		}
    	}
    };
    
    
	private void mostrarDialogo(String titulo, String mensaje){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(mensaje).setTitle(titulo).setNegativeButton("OK", null);
    	AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	
	private void ventanaControlṔresion(){
		Intent intent = new Intent(this, ControlPresionArterialActivity.class);
		intent.putExtra("Token", token);
		this.finish();
		startActivity(intent);
	}
}