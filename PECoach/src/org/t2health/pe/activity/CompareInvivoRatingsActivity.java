package org.t2health.pe.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import org.t2health.pe.R;
import org.t2health.pe.tables.Invivo;
import org.t2health.pe.tables.Rating;
import org.t2health.pe.tables.UserQAAnswer;

import zencharts.charts.DateChart;
import zencharts.data.DatePoint;
import zencharts.data.DateSeries;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CompareInvivoRatingsActivity extends ABSSessionNavigationActivity {
	private static final String KEY_TITLE = "title";
	private static final String KEY_POST_RATING = "postRating";
	private static final String KEY_PRE_RATING = "preRating";

	//Graphing vars
	public DateChart dateChart;
	public boolean chartMode = false;
	private ListView lvReport;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set this content view.
		this.setContentView(R.layout.suds_report_activity);
		lvReport = ((ListView)this.findViewById(R.id.list));
		
		dateChart = (DateChart)this.findViewById(R.id.datechart);
		dateChart.LoadFont("Elronmonospace.ttf", 16, 2, 2);
//        dateChart.showGrid = true;
//        dateChart.scrollGrid = false;
//        dateChart.showStars = false;
//        dateChart.screenPoints = 4;
        //dateChart.setMinMaxYValue(0, 100);
        dateChart.setVisibility(View.GONE);
        
        this.setRightButtonText("Chart");
		this.setRightButtonVisibility(View.VISIBLE);
		this.setToolboxButtonVisibility(View.GONE);

		Random color = new Random();

		//Color randomColor = new Color(color.nextInt(256),color.nextInt(256),color.nextInt(256));
		
		ArrayList<HashMap<String,Object>> items = new ArrayList<HashMap<String,Object>>();
		ArrayList<Invivo> invivos = session.getSessionGroup().getInVivos();
		for(int i = 0; i < invivos.size(); ++i) {
			Invivo invivo = invivos.get(i);
			
			DateSeries sudsSeries = new DateSeries(this, R.drawable.quadstar);
	        //sudsSeries.dashEffect = new float[] {i,20};
	        sudsSeries.lineColor = Color.BLUE;
	        sudsSeries.lineWidth = 5;
	        sudsSeries.dateLabels = false;
			DatePoint tmpPointa = new DatePoint(Calendar.getInstance().getTimeInMillis(), 0, "");
			DatePoint tmpPointb = new DatePoint(Calendar.getInstance().getTimeInMillis(), 0, "");
			HashMap<String,Object> item = new HashMap<String,Object>();
			item.put(KEY_TITLE, invivo.title);
			
			ArrayList<Rating> ratings = invivo.getRatings();
			if(ratings.size() > 0) {
				Rating rating = ratings.get(0);
				item.put(KEY_PRE_RATING, rating.preValue<0?"":rating.preValue+"");
				item.put(KEY_POST_RATING, rating.postValue<0?"":rating.postValue+"");
				
				tmpPointa.label = invivo.title;
				tmpPointa.timeStamp = rating.preTimestamp;
				tmpPointa.value = rating.preValue;
				tmpPointb.label = invivo.title;
				tmpPointb.timeStamp = rating.postTimestamp;
				tmpPointb.value = rating.postValue;
			}
			items.add(item);
			sudsSeries.add(tmpPointa);
			sudsSeries.add(tmpPointb);
			dateChart.AddSeries(sudsSeries);
		}

		ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.list_item_compare_rating, null);
		((TextView)headerView.findViewById(R.id.text2)).setText("Second");
		((TextView)headerView.findViewById(R.id.text3)).setText("Final");
		lvReport.addHeaderView(headerView);

		lvReport.setAdapter(new SimpleAdapter(
				this,
				items,
				R.layout.list_item_compare_rating,
				new String[] {
						KEY_TITLE,
						KEY_PRE_RATING,
						KEY_POST_RATING,
				},
				new int[] {
						R.id.text1,
						R.id.text2,
						R.id.text3,
				}
				));
	}
	
	@Override
	protected void onRightButtonPressed() {
		if(chartMode)
		{
			chartMode = false;
			dateChart.setVisibility(View.GONE);
			this.setRightButtonText("Chart");
		}
		else
		{
			chartMode = true;
			dateChart.setVisibility(View.VISIBLE);			
			this.setRightButtonText("List");
		}
	}

}