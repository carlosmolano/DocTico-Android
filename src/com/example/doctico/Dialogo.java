package com.example.doctico;

import android.app.AlertDialog;
import android.content.Context;

public class Dialogo {
	
	public void mostrar(String titulo, String mensaje, Context contexto){
		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
    	builder.setMessage(mensaje).setTitle(titulo).setNegativeButton("OK", null);
    	AlertDialog dialog = builder.create();
		dialog.show();
	}

}
