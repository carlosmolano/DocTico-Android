package com.example.doctico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.doctico.AccesoDatos.JSONParser;
import com.example.doctico.Ayudas.Dialogo;

import android.app.Activity;
import android.app.ProgressDialog;
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
	private Dialogo dialogo;
    private ProgressDialog progress;
    ArrayList<String> lista_centros;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_cita);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
	    token = getIntent().getExtras().getString("Token");
	    lista_centros = getIntent().getExtras().getStringArrayList("Lista_Centros");
	    dialogo = new Dialogo();
		miIndentificadorCita = (EditText) findViewById(R.id.entrada_nombre_cita);
		miTiempo = (EditText) findViewById(R.id.texto_hora);
		miFecha = (EditText) findViewById(R.id.texto_fecha);
		miCentro = (Spinner) findViewById(R.id.spinner);

		boton_agregar_cita = (Button)findViewById(R.id.btn_agregar_cita);      
		boton_agregar_cita.setOnClickListener(Eventos_Botones);   
		
        progress = new ProgressDialog(this);
        progress.setTitle("Por favor espere!!");
        progress.setMessage("Agregando la nueva cita...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
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
		    	progress.show();
        	    new Thread(){
                    public void run(){
            			String identificador_cita = miIndentificadorCita.getText().toString();
            			String hora = miTiempo.getText().toString();
            			String fecha = miFecha.getText().toString();
            			String centro = miCentro.getSelectedItem().toString();
            			
            			JSONParser jsonparser = new JSONParser();
            			String respuesta = jsonparser.agregar_nueva_cita(token, identificador_cita, hora, fecha, centro);
            	        
            			System.out.println(respuesta);
            			
            			if(respuesta.equals("Si"))
        	                    	VentanaCitas();	
                    }
                }.start();
   	       }
    	}
    };
    
    
    private void VentanaCitas(){
    	Intent i = new Intent(this, ControlCitasActivity.class);
		i.putExtra("Token", token);
		i.putStringArrayListExtra("Lista_Citas", obtener_citas(token));
		this.finish();
    	startActivity(i);
    	progress.dismiss();
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
	
	
	private void errorAgregarCita(){
		dialogo.mostrar("Upps...", "Ha ocurrido un error y No se pudo agregar la nueva cita, intentelo otra vez...", this);
	}
    
    
	private void cargarCentros(){        
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_centros);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		miCentro.setAdapter(dataAdapter);
	}
}
