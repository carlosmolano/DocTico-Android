package com.example.doctico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import android.widget.Button;
import android.widget.EditText;

public class AgregarMuestraPresionArterialActivity extends Activity {
	
	private EditText miTiempo;
	private EditText miFecha;
	private EditText miSistolica;
	private EditText miDiastolica;
	private String token;
	private Dialogo dialogo;
	private Button boton_agregar_muestra;
    private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_muestra_presion_arterial);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		token = getIntent().getExtras().getString("Token");
		dialogo = new Dialogo();
		miTiempo = (EditText) findViewById(R.id.texto_hora);
		miFecha = (EditText) findViewById(R.id.texto_fecha);
		miSistolica = (EditText) findViewById(R.id.texto_sistolica);
		miDiastolica = (EditText) findViewById(R.id.texto_diastolica);
		
		boton_agregar_muestra = (Button)findViewById(R.id.btn_agregar_muestra);      
		boton_agregar_muestra.setOnClickListener(Eventos_Botones);   
		
        progress = new ProgressDialog(this);
        progress.setTitle("Por favor espere!!");
        progress.setMessage("Agregando la nueva muestra de presion arterial...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Date now = new Date(); // java.util.Date, NOT java.sql.Date or java.sql.Timestamp!
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(now);
        String hora = new SimpleDateFormat("HH:mm").format(now);
	
        miTiempo.setText(hora);
        miFecha.setText(fecha);
        
		miSistolica.setText("120");
		miDiastolica.setText("60");
	}
	
		
    private OnClickListener Eventos_Botones = new OnClickListener()    // Metodo de evento de botones
    {
    	public void onClick(final View v)
    	{
    		if(v.getId() == boton_agregar_muestra.getId())            // Evento de Ingresar a la aplicaci�n
    		{
		    	progress.show();
        	    new Thread(){
                    public void run(){
		    			JSONParser jsonparser = new JSONParser();
		    			String respuesta = jsonparser.agregar_muestra_presion(token, miTiempo.getText().toString(), 
		    					           miFecha.getText().toString(), miSistolica.getText().toString(), miDiastolica.getText().toString());		
		    			
		    			if(respuesta.equals("Si"))
		    				ventanaControlṔresion();
                    }
                }.start();
    		}
    	}
    };
    
    
	private ArrayList<String> obtener_muestras_presion_arterial(String token)
	{
		JSONParser jParser = new JSONParser();
	    JSONArray json = jParser.getJSONFromUrl("http://doctico.herokuapp.com/api/api_doc_tico/presion_arterial.json?token=" + token);         // get JSON data from URL
	    String fecha;
	    String sistolica;
	    String diastolica;
	    JSONObject muestra_actual;
	    
        ArrayList<String> lista_muestras = new ArrayList<String>();       		
        int cantidad_muestras = json.length();
        
	    if(cantidad_muestras > 0){
	        for (int i = 0; i < cantidad_muestras; i++) {
		        try {
		        	muestra_actual = json.getJSONObject(i);	       
		        	fecha = muestra_actual.get("fecha").toString();
		        	sistolica = muestra_actual.get("sistolica").toString();
		        	diastolica = muestra_actual.get("diastolica").toString();
		        	lista_muestras.add("     " + sistolica + "            " + diastolica + "            " + fecha);
		        }
		        catch (JSONException e) {
		            e.printStackTrace();
		        }
		    }
	    }
	    return lista_muestras;
	}
    
    
	private void errorAgregarMuestra(){
		dialogo.mostrar("Upps...", "Ha ocurrido un error y No se pudo agregar la nueva muestra, intentelo otra vez...", this);
	}
	
	
	private void ventanaControlṔresion(){
		Intent intent = new Intent(this, ControlPresionArterialActivity.class);
		intent.putExtra("Token", token);
		intent.putStringArrayListExtra("Lista_Muestras", obtener_muestras_presion_arterial(token));
		this.finish();
		startActivity(intent);
		progress.dismiss();
	}
}