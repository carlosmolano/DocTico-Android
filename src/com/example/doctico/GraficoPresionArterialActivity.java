package com.example.doctico;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class GraficoPresionArterialActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grafico_presion_arterial);

		// init example series data
		GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
		    new GraphViewData(1, 120)
		    , new GraphViewData(2, 130)
		    , new GraphViewData(3, 120)
		    , new GraphViewData(4, 140)
		    , new GraphViewData(5, 130)
		    , new GraphViewData(6, 120)
		    , new GraphViewData(7, 130)
		    //, new GraphViewData(8, 150)
		});
		
		GraphViewSeries example2Series = new GraphViewSeries(new GraphViewData[] {
			    new GraphViewData(1, 80)
			    , new GraphViewData(2, 70)
			    , new GraphViewData(3, 60)
			    , new GraphViewData(4, 70)
			    , new GraphViewData(5, 75)
			    , new GraphViewData(6, 84)
			    , new GraphViewData(7, 70)
			   // , new GraphViewData(8, 50)
			});
		
		 
		GraphView graphView = new LineGraphView(this, "");
		
		//graphView.getGraphViewStyle().setGridColor(Color.GREEN);
		//graphView.getGraphViewStyle().setVerticalLabelsColor(Color.GREEN);
		
		//graphView.getGraphViewStyle().setNumHorizontalLabels(2);
		graphView.getGraphViewStyle().setNumVerticalLabels(11);
		
		graphView.getGraphViewStyle().setNumHorizontalLabels(7);
		
		
		//graphView.setHorizontalLabels(new String[] {"2 days ago", "yesterday", "today", "tomorrow"});
		//graphView.setVerticalLabels(new String[] {"150", "140", "130", "120", "110", "100", "90", "80", "70", "60", "50"});
		
		graphView.addSeries(exampleSeries); // data
		graphView.addSeries(example2Series); // data
		 
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		layout.addView(graphView);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.grafico_presion_arterial, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
