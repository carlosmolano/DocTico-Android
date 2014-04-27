package com.example.doctico;

import com.example.doctico.AccesoDatos.JSONParser;
import com.example.doctico.Ayudas.Dialogo;
import com.example.doctico.Ayudas.Estado;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class IniciarSesionActivity extends Activity {
	
	private Button boton_iniciar_sesion;        
	private EditText email;           // Entrada del email
	private EditText contraseña;      // Entrada del password
	private TextView mensaje_error;
	private Dialogo dialogo;
	private Estado estado;
	private ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iniciar_sesion);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		dialogo = new Dialogo();
		estado = new Estado();
		
        progress = new ProgressDialog(this);
        progress.setTitle("Por favor espere!!");
        progress.setMessage("Iniciando Sesion");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		boton_iniciar_sesion = (Button)findViewById(R.id.btn_inciar_sesion);      
		boton_iniciar_sesion.setOnClickListener(Eventos_Botones);                // Llamar a los eventos
		mensaje_error = (TextView) findViewById(R.id.mensaje_error);
		
		email = (EditText)findViewById(R.id.entrada_email);               // Obtener el valor del imput del correo
		contraseña = (EditText)findViewById(R.id.entrada_password);       // Obtener el valor del input de contrase�a
	}	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.iniciar_sesion, menu);
	    return true;	    
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.crear_cuenta){
			if(estado.ConexionInternetDisponible((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE))){	
				Intent intent = new Intent(this, CrearCuentaActivity.class);
				startActivity(intent);
				return true;
			}
			else
				errorConexionInternet();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
    private OnClickListener Eventos_Botones = new OnClickListener()    // Metodo de evento de botones
    {
    	public void onClick(final View v)
    	{
    		if(v.getId() == boton_iniciar_sesion.getId())            // Evento de Ingresar a la aplicaci�n
    		{
    			if(estado.ConexionInternetDisponible((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)))
    			{	
    	    	    progress.show();
    	    	    new Thread(){
    	                public void run(){
			    			JSONParser jsonparser = new JSONParser();
			    			String token = jsonparser.autenticar_usuario(email.getText().toString(), contraseña.getText().toString());
			    			
			    			progress.dismiss();
			    			if(!token.equals("")){
			        		 	Ventana_Menu_Funcionalidades(token);
			    			}
    	                }
    	            }.start();
    	            errorIniciarSesion();
    			}
    			else{
    				errorConexionInternet();
    			}
    		}
         }
   };
         
    
    
	private void errorIniciarSesion(){
        mensaje_error.setText("Datos Invalidos....");
        contraseña.setText("");
	}
	
	
	private void errorConexionInternet(){
		dialogo.mostrar("Internet", "Se requiere Internet para completar esta transaccion!", this);
	}	
	
	
    private void Ventana_Menu_Funcionalidades(String token){
		Intent intent = new Intent(this, MenuFuncionalidadesActivity.class);
		intent.putExtra("Token", token);
		this.finish();
		startActivity(intent);
    }
}