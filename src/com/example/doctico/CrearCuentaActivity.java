package com.example.doctico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CrearCuentaActivity extends Activity {

	private Button boton_crear_cuenta;
	
	private String nombre;
	private String email;				  
	private String contraseña;
	private String confirmar_contraseña;
	
	private EditText entrada_nombre;          
	private EditText entrada_email;          
	private EditText entrada_contraseña;     
	private EditText entrada_confirmar_contraseña;   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_cuenta);
		
		boton_crear_cuenta = (Button)findViewById(R.id.btn_crear_cuenta);      
		boton_crear_cuenta.setOnClickListener(Eventos_Botones);                // Llamar a los eventos
		
		entrada_nombre = (EditText)findViewById(R.id.entrada_nombre_crear_cuenta);      
		entrada_email = (EditText)findViewById(R.id.entrada_email_crear_cuenta);
		entrada_contraseña = (EditText)findViewById(R.id.entrada_password_crear_cuenta);      
		entrada_confirmar_contraseña = (EditText)findViewById(R.id.entrada_confirmar_password_crear_cuenta);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crear_cuenta, menu);
		return true;
	}
	
	
    private OnClickListener Eventos_Botones = new OnClickListener()    // Metodo de evento de botones
    {
    	public void onClick(final View v)
    	{
    		if(v.getId() == boton_crear_cuenta.getId())            // Evento de Ingresar a la aplicaci�n
    		{
    			nombre = entrada_nombre.getText().toString();
    			email = entrada_email.getText().toString();
    			contraseña = entrada_contraseña.getText().toString();
    			confirmar_contraseña = entrada_confirmar_contraseña.getText().toString();
    			System.out.println(nombre);
    			System.out.println(email);
    			System.out.println(contraseña);
    			System.out.println(confirmar_contraseña);
    			
    			irVentanaInicio(v);
    		}
    	}
    };
    
    
    private void irVentanaInicio(View v)
    {
		Intent i = new Intent(this, IniciarSesionActivity.class);
		startActivity(i);
    }
}
