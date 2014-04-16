package com.example.doctico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AgregarCitaActivity extends Activity {
	
	private EditText miIndentificadorCita;
	private EditText miTiempo;
	private EditText miFecha;
	private Spinner miCentro;
	private Button boton_agregar_cita;
	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_cita);
		
	    Bundle bundle = getIntent().getExtras();
	    token = bundle.getString("Token");
	    System.out.println(bundle.getString("Token"));
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 

		miIndentificadorCita = (EditText) findViewById(R.id.entrada_nombre_cita);
		miTiempo = (EditText) findViewById(R.id.texto_hora);
		miFecha = (EditText) findViewById(R.id.texto_fecha);
		miCentro = (Spinner) findViewById(R.id.spinner);

		boton_agregar_cita = (Button)findViewById(R.id.btn_agregar_cita);      
		boton_agregar_cita.setOnClickListener(Eventos_Botones);   
		
		Calendar calendar = Calendar.getInstance();
		miTiempo.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
		miFecha.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" +  calendar.get(Calendar.YEAR));
		cargarCentros();
	}
	
	
    private OnClickListener Eventos_Botones = new OnClickListener()    // Metodo de evento de botones
    {
    	public void onClick(final View v){
    		if(v.getId() == boton_agregar_cita.getId())            // Evento de Ingresar a la aplicaci�n
    		{
    			String identificador_cita = miIndentificadorCita.getText().toString();
    			String hora = miTiempo.getText().toString();
    			String fecha = miFecha.getText().toString();
    			String centro = miCentro.getSelectedItem().toString();
    			
    			System.out.println(identificador_cita);
    			System.out.println(hora);
    			System.out.println(fecha);
    			System.out.println(centro);
    			
    			JSONParser jsonparser = new JSONParser();
    			String respuesta = jsonparser.agregar_nueva_cita(token, identificador_cita, hora, fecha, centro);
    	        
    			System.out.println(respuesta);
    			
    			if(respuesta.equals("Si")){
    				mostrarDialogo(":)", "Se ha agregado correctamente tu nueva cita");  
    				ventanaControlCitas();
    			}
    			else
    				mostrarDialogo("Upps...", "Ha ocurrido un error y No se pudo agregar la nueva cita, intentelo otra vez...");   
    		}
    	}
    };
    
    
	private void cargarCentros(){
		List<String> lista_centros = new ArrayList<String>();
		lista_centros.add("Centro por Defecto");
		
		JSONParser jParser = new JSONParser();
        JSONArray json = jParser.getJSONFromUrl("http://doctico.herokuapp.com/api/api_doc_tico/nombres_centros_salud.json?token=" + token);         // get JSON data from URL

        for (int i = 0; i < json.length(); i++) {
	        try {            
	            lista_centros.add(json.getJSONObject(i).get("nombre").toString());
	        }
	        catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }
        
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_centros);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		miCentro.setAdapter(dataAdapter);
	}
    
    
	private void mostrarDialogo(String titulo, String mensaje){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(mensaje).setTitle(titulo).setNegativeButton("OK", null);
    	AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	
	private void ventanaControlCitas(){
		Intent intent = new Intent(this, ControlCitasActivity.class);
		intent.putExtra("Token", token);
		this.finish();
		startActivity(intent);
	}
}
