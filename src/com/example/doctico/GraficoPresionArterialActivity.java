package com.example.doctico;

import java.util.ArrayList;

import com.example.doctico.Ayudas.GraphViewData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class GraficoPresionArterialActivity extends Activity {
	
    ArrayList<String> diastolicas;
    ArrayList<String> sistolicas;
    ArrayList<String> fechas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grafico_presion_arterial);

        getActionBar().hide();
	    sistolicas = getIntent().getExtras().getStringArrayList("Sistolicas");
	    diastolicas = getIntent().getExtras().getStringArrayList("Diastolicas");
	    fechas = getIntent().getExtras().getStringArrayList("Fechas");
	    generar_grafico();
	}
	
	private void generar_grafico()
	{
		GraphView grafico_presion_arterial = new LineGraphView(this, "Historial Presion Arterial");
	    int cantidad_muestras = sistolicas.size();
	    
	    GraphViewData[] datos_sistolicas = new GraphViewData[cantidad_muestras];
	    GraphViewData[] datos_diastolicas = new GraphViewData[cantidad_muestras];
	    
        for (int i = 0; i < cantidad_muestras; i++) {    
	        	datos_sistolicas[i] = new GraphViewData(i, Integer.parseInt(sistolicas.get(i)));
	        	datos_diastolicas[i] = new GraphViewData(i, Integer.parseInt(diastolicas.get(i)));
	    }

	    grafico_presion_arterial.addSeries(new GraphViewSeries(datos_sistolicas));
	    grafico_presion_arterial.addSeries(new GraphViewSeries(datos_diastolicas));
	    grafico_presion_arterial.getGraphViewStyle().setNumVerticalLabels(11);	
	    
    	grafico_presion_arterial.setHorizontalLabels
        (new String[] {
        		fechas.get(0).toString().substring(0, 5),
        		fechas.get((cantidad_muestras/2)/2).toString().substring(0, 5),
        		fechas.get(cantidad_muestras/2).toString().substring(0, 5),
        		fechas.get((cantidad_muestras/2) + (cantidad_muestras/2)/2).toString().substring(0, 5),
        		fechas.get(cantidad_muestras-1).toString().substring(0, 5)
        });
	    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		layout.addView(grafico_presion_arterial);
	}
}
