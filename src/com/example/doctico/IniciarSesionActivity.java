package com.example.doctico;

import com.example.doctico.AccesoDatos.JSONParser;
import com.example.doctico.Ayudas.Dialogo;
import com.example.doctico.Ayudas.Estado;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class IniciarSesionActivity extends Activity {
	
	private Button boton_iniciar_sesion;        
	private EditText email;           // Entrada del email
	private EditText contraseña;      // Entrada del password
	private Dialogo dialogo;
	private Estado estado;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iniciar_sesion);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		dialogo = new Dialogo();
		estado = new Estado();
		
		boton_iniciar_sesion = (Button)findViewById(R.id.btn_inciar_sesion);      
		boton_iniciar_sesion.setOnClickListener(Eventos_Botones);                // Llamar a los eventos
		
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
	    			JSONParser jsonparser = new JSONParser();
	    			String token = jsonparser.autenticar_usuario(email.getText().toString(), contraseña.getText().toString());
	    			
	    			if(!token.equals(""))
	        		 	Ventana_Menu_Funcionalidades(token);
	    			else
	    				errorIniciarSesion();
    			}
    			else
    				errorConexionInternet();
    		}
    	}
    };
    
    
	private void errorIniciarSesion(){
		dialogo.mostrar("Error al Iniciar Sesion", "Los datos no son correctos, intentelo otra vez...", this);
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