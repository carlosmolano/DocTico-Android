package com.example.doctico;

import com.example.doctico.AccesoDatos.JSONParser;
import com.example.doctico.Ayudas.Dialogo;
import com.example.doctico.Ayudas.Estado;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CrearCuentaActivity extends Activity {

	private Button boton_crear_cuenta;
	private EditText nombre;
	private EditText email;				  
	private EditText contraseña;  
	private EditText contraseña_2;  
	private Dialogo dialogo;
	private Estado estado;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_cuenta);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		dialogo = new Dialogo();
		estado = new Estado();
		
		boton_crear_cuenta = (Button)findViewById(R.id.btn_crear_cuenta);      
		boton_crear_cuenta.setOnClickListener(Eventos_Botones);                // Llamar a los eventos
		
		nombre = (EditText)findViewById(R.id.entrada_nombre_crear_cuenta);      
		email = (EditText)findViewById(R.id.entrada_email_crear_cuenta);
		contraseña = (EditText)findViewById(R.id.entrada_password_crear_cuenta);      
		contraseña_2 = (EditText)findViewById(R.id.entrada_confirmar_password_crear_cuenta);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.crear_cuenta, menu);
		return true;
	}
	
	
    private OnClickListener Eventos_Botones = new OnClickListener()    // Metodo de evento de botones
    {
    	public void onClick(final View v)
    	{
    		if(v.getId() == boton_crear_cuenta.getId())        
    		{
    			if(estado.ConexionInternetDisponible((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)))
    			{	
	    			JSONParser jsonparser = new JSONParser();
	    			String respuesta = jsonparser.crear_usuario(nombre.getText().toString(), email.getText().toString(), 
	    					                                    contraseña.getText().toString(), contraseña_2.getText().toString());
	    			
	    			if(respuesta.equals("Si")){
	    				finalizarEstaVentana();
	    			}
	    			else
	    				errorCrearCuenta();
    			}
    			else
    				errorConexionInternet();
    		}
    	}
    };
    
    
	private void errorCrearCuenta(){
		dialogo.mostrar("Error al Crear Cuenta", "No se pudo crear la cuenta, intentelo otra vez...", this);
		contraseña.setText("");
		contraseña_2.setText("");
	}
	
	
	private void errorConexionInternet(){
		dialogo.mostrar("Internet", "Se requiere Internet para completar esta transaccion!", this);
	}	

   	
	private void finalizarEstaVentana(){
		this.finish();
	}
}
