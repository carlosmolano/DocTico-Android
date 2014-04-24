package com.example.doctico;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.doctico.AccesoDatos.JSONParser;
import com.example.doctico.Ayudas.GraphViewData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class GraficoPresionArterialActivity extends Activity {
	
    private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grafico_presion_arterial);

        getActionBar().hide();
	    token = getIntent().getExtras().getString("Token");
	    
	    generar_grafico_muestras_presion_arterial(token);
	}
	
	
	private void generar_grafico_muestras_presion_arterial(String token)
	{
		GraphView grafico_presion_arterial = new LineGraphView(this, "Historial Presion Arterial");
		JSONParser jParser = new JSONParser();
	    JSONArray json = jParser.getJSONFromUrl("http://doctico.herokuapp.com/api/api_doc_tico/presion_arterial.json?token=" + token);
	    JSONObject muestra_actual;

	    int cantidad_muestras = json.length();
	    
	    GraphViewData[] datos_sistolicas = new GraphViewData[cantidad_muestras];
	    GraphViewData[] datos_diastolicas = new GraphViewData[cantidad_muestras];
	    
        for (int i = 0; i < cantidad_muestras; i++) {
	        try {
	        	muestra_actual = json.getJSONObject(i);	      
	        	datos_sistolicas[i] = new GraphViewData(i, Integer.parseInt(muestra_actual.get("sistolica").toString()));
	        	datos_diastolicas[i] = new GraphViewData(i, Integer.parseInt(muestra_actual.get("diastolica").toString()));
	        }
	        catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }

	    grafico_presion_arterial.addSeries(new GraphViewSeries(datos_sistolicas));
	    grafico_presion_arterial.addSeries(new GraphViewSeries(datos_diastolicas));
	    grafico_presion_arterial.getGraphViewStyle().setNumVerticalLabels(11);
		
	    try {
	    	grafico_presion_arterial.setHorizontalLabels
			                  (new String[] {
			                		  json.getJSONObject(0).get("fecha").toString().substring(0, 4),
			                		  json.getJSONObject((cantidad_muestras/2)/2).get("fecha").toString().substring(0, 4),
					                  json.getJSONObject(cantidad_muestras/2).get("fecha").toString().substring(0, 4),
					                  json.getJSONObject((cantidad_muestras/2) + (cantidad_muestras/2)/2).get("fecha").toString().substring(0, 4),
					                  json.getJSONObject(cantidad_muestras-1).get("fecha").toString().substring(0, 4)});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    
	    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		layout.addView(grafico_presion_arterial);
	}
}
