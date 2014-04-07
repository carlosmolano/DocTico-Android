package com.example.doctico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class IniciarSesionActivity extends Activity {
	
	private Button boton_iniciar_sesion;             
	private Button boton_crear_cuenta;        
	private String email;				       // Para guardar el email digitado por el usuario
	private String password;				   // Para guardar el password digitado por el usuario
	private EditText entrada_email;           // Entrada del username
	private EditText entrada_contraseña;      // Entrada del password
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iniciar_sesion);
		
		boton_iniciar_sesion = (Button)findViewById(R.id.btn_inciar_sesion);      
		boton_iniciar_sesion.setOnClickListener(Eventos_Botones);                // Llamar a los eventos
		
		boton_crear_cuenta = (Button)findViewById(R.id.btn_to_crear_cuenta);     
		boton_crear_cuenta.setOnClickListener(Eventos_Botones);                  // Llamar a los eventos
		
		entrada_email = (EditText)findViewById(R.id.entrada_email);               // Obtener el valor del imput del correo
		entrada_contraseña = (EditText)findViewById(R.id.entrada_password);       // Obtener el valor del input de contrase�a
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.iniciar_sesion, menu);
		return true;
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
    			
    		 	Ventana_Menu_Funcionalidades(v);
    		}
    		
    		else if(v.getId() == boton_crear_cuenta.getId())          // Evento de crear una cuenta
    			Ventana_Crear_Cuenta(v);
    	}
    };
	
	
    public void Ventana_Crear_Cuenta(View view)	    		// Siguiente ventana del software
    {
		Intent i = new Intent(this, CrearCuentaActivity.class);
		startActivity(i);
    }
    
    public void Ventana_Menu_Funcionalidades(View view)	    		// Siguiente ventana del software
    {
		Intent i = new Intent(this, MenuFuncionalidadesActivity.class);
		startActivity(i);
    }

}
