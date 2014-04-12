package com.example.doctico;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AgregarCitaActivity extends Activity {
	
	private EditText miIndentificadorCita;
	private EditText miTiempo;
	private EditText miFecha;
	private Button boton_agregar_cita;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_cita);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 

		miTiempo = (EditText) findViewById(R.id.texto_hora);
		miFecha = (EditText) findViewById(R.id.texto_fecha);
		miIndentificadorCita = (EditText) findViewById(R.id.entrada_nombre_cita);
		
		boton_agregar_cita = (Button)findViewById(R.id.btn_agregar_cita);      
		boton_agregar_cita.setOnClickListener(Eventos_Botones);   
		
		Calendar calendar = Calendar.getInstance();
		miTiempo.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
		miFecha.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" +  calendar.get(Calendar.YEAR));
	}
	
	
    private OnClickListener Eventos_Botones = new OnClickListener()    // Metodo de evento de botones
    {
    	public void onClick(final View v){
    		if(v.getId() == boton_agregar_cita.getId())            // Evento de Ingresar a la aplicaci�n
    		{
    			String identificador_cita = miIndentificadorCita.getText().toString();
    			String hora = miTiempo.getText().toString();
    			String fecha = miFecha.getText().toString();
    			
    			System.out.println(hora);
    			System.out.println(fecha);
    			
    			JSONParser jsonparser = new JSONParser();
    			String respuesta = jsonparser.agregar_nueva_cita(identificador_cita, hora, fecha, "", "");
    	        
    			System.out.println(respuesta);
    			
    			if(respuesta.equals("Si")){
    				mostrarDialogo(":)", "Se ha agregado correctamente tu nueva cita");  
    				//irVentanaInicio(v);
    			}
    			else
    				mostrarDialogo("Upps...", "Ha ocurrido un error y No se pudo agregar la nueva cita, intentelo otra vez...");   
    		}
    	}
    };
    
    
	private void mostrarDialogo(String titulo, String mensaje)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(mensaje)
    	       .setTitle(titulo)
    	       .setNegativeButton("OK", null);
    	AlertDialog dialog = builder.create();
		dialog.show();
	}
}
