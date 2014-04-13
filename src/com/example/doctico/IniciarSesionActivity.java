package com.example.doctico;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
	private String email;				       // Para guardar el email digitado por el usuario
	private String password;				   // Para guardar el password digitado por el usuario
	private EditText entrada_email;           // Entrada del username
	private EditText entrada_contraseña;      // Entrada del password
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iniciar_sesion);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		boton_iniciar_sesion = (Button)findViewById(R.id.btn_inciar_sesion);      
		boton_iniciar_sesion.setOnClickListener(Eventos_Botones);                // Llamar a los eventos
		
		entrada_email = (EditText)findViewById(R.id.entrada_email);               // Obtener el valor del imput del correo
		entrada_contraseña = (EditText)findViewById(R.id.entrada_password);       // Obtener el valor del input de contrase�a
	}	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.iniciar_sesion, menu);
	    return true;	    
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.crear_cuenta) {
		      Intent intent = new Intent(this, CrearCuentaActivity.class);
		      startActivity(intent);
			  return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
    private OnClickListener Eventos_Botones = new OnClickListener()    // Metodo de evento de botones
    {
    	public void onClick(final View v)
    	{
    		if(v.getId() == boton_iniciar_sesion.getId())            // Evento de Ingresar a la aplicaci�n
    		{
    			email = entrada_email.getText().toString();
    			password = entrada_contraseña.getText().toString();
    			System.out.println(email);
    			System.out.println(password);
    			
    			JSONParser jsonparser = new JSONParser();
    			String respuesta = jsonparser.autenticar_usuario(email, password);
    			System.out.println(respuesta);
    			
    			if(!respuesta.equals(""))
        		 	Ventana_Menu_Funcionalidades(v, respuesta);
    			else{
    				mostrarDialogo("Error al Iniciar Sesion", "Los datos no son correctos, intentelo otra vez...");   
    				entrada_contraseña.setText("");
    			}
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
	
    
    private void Ventana_Menu_Funcionalidades(View view, String token){
		Intent intent = new Intent(this, MenuFuncionalidadesActivity.class);
		intent.putExtra("Token", token);
		this.finish();
		startActivity(intent);
    }
}