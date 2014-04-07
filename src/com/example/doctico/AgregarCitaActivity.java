package com.example.doctico;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class AgregarCitaActivity extends Activity {
	
	EditText miTiempo;
	EditText miFecha;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_cita);

		miTiempo = (EditText) findViewById(R.id.texto_hora);
		miFecha = (EditText) findViewById(R.id.texto_fecha);
		
		Calendar calendar = Calendar.getInstance();
		miTiempo.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
		miFecha.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" +  calendar.get(Calendar.YEAR));
		
	}
}
