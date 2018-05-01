package com.ublox.BLE.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ublox.BLE.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Activity_Graph extends Activity {

    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_graph);

        Intent i = getIntent();
        List<Entry> entries  = (List<Entry>)i.getSerializableExtra("Graph");
        int chHi = (int)i.getSerializableExtra("High");
        int chLo = (int)i.getSerializableExtra("Low");
        int Number = (int) i.getSerializableExtra("Number");
        chart = (LineChart) findViewById(R.id.chart);



        if(Number == 0) {

            LineDataSet dataSet = new LineDataSet(entries, "Temperature °C"); // add entries to dataset
            dataSet.setCircleColor(Color.argb(150, 10, 109, 255));
            dataSet.setDrawValues(false);//disable y axis value appearing near the line
            dataSet.setLineWidth(3f);
            dataSet.setColor(Color.argb(150, 10, 12, 247));
            dataSet.setCircleRadius(2f);
            dataSet.setHighLightColor(Color.BLACK);
            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            YAxis yAxis = chart.getAxisLeft();
            yAxis.setLabelCount(10, true);


            LimitLine upperLimit = new LimitLine(chHi / 10, "Upper Limit "+ chHi / 10 + " °C");
            upperLimit.setLineWidth(2f);
            upperLimit.enableDashedLine(10f, 10f, 0f);
            upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

            LimitLine lowerLimit = new LimitLine(chLo / 10, "Lower Limit "+ chLo / 10 + " °C");
            lowerLimit.setLineWidth(2f);
            lowerLimit.enableDashedLine(10f, 10f, 0f);
            lowerLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new IAxisValueFormatter() {

                private SimpleDateFormat mFormat = new SimpleDateFormat("MMM dd HH:mm:ss");

                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    long millis = TimeUnit.MILLISECONDS.toMillis((long) value);
                    return mFormat.format(new Date(millis));
                }
            });
            xAxis.setLabelCount(3);
            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.removeAllLimitLines();
            leftAxis.addLimitLine(upperLimit);
            leftAxis.addLimitLine(lowerLimit);
            leftAxis.setAxisMaximum(120f);
            leftAxis.setAxisMinimum(-90f);
            leftAxis.enableGridDashedLine(10f, 10f, 0);
            leftAxis.setDrawLimitLinesBehindData(true);
            chart.getAxisRight().setEnabled(false);
            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    Toast.makeText(Activity_Graph.this,"Date/Time\t\t:" + chart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), chart.getXAxis()) + "\nTemperature :\t" + chart.getAxisLeft().getValueFormatter().getFormattedValue(e.getY(), chart.getAxisLeft()) + " °C", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });

            chart.invalidate();
        }else if(Number == 1){
            LineDataSet dataSet = new LineDataSet(entries, "Humidity %RH"); // add entries to dataset
            dataSet.setCircleColor(Color.argb(150, 10, 109, 255));
            dataSet.setLineWidth(3f);
            dataSet.setDrawValues(false);//disable y axis value appearing near the line
            dataSet.setColor(Color.argb(150, 10, 12, 247));
            dataSet.setCircleRadius(2f);
            dataSet.setHighLightColor(Color.BLACK);
            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);


            LimitLine upperLimit = new LimitLine(chHi / 10, "Upper Limit "+ chHi / 10 + " %RH");
            upperLimit.setLineWidth(2f);
            upperLimit.enableDashedLine(10f, 10f, 0f);
            upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

            LimitLine lowerLimit = new LimitLine(chLo / 10, "Lower Limit "+ chLo / 10 + " %RH");
            lowerLimit.setLineWidth(2f);
            lowerLimit.enableDashedLine(10f, 10f, 0f);
            lowerLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new IAxisValueFormatter() {

                private SimpleDateFormat mFormat = new SimpleDateFormat("MMM dd HH:mm:ss");

                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    long millis = TimeUnit.MILLISECONDS.toMillis((long) value);
                    return mFormat.format(new Date(millis));
                }
            });
            xAxis.setLabelCount(3);
            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.removeAllLimitLines();
            leftAxis.addLimitLine(upperLimit);
            leftAxis.addLimitLine(lowerLimit);
            leftAxis.setAxisMaximum(120f);
            leftAxis.setAxisMinimum(-10f);
            leftAxis.enableGridDashedLine(10f, 10f, 0);
            leftAxis.setDrawLimitLinesBehindData(true);
            chart.getAxisRight().setEnabled(false);

            chart.invalidate();

            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    //Log.d("____", "VALUE IS SELECTED " +  + " " + );
                    //Toast.makeText(Activity_Read.this, +"Temperature :\t" + Integer.parseInt(chart.getAxisLeft().getValueFormatter().getFormattedValue(e.getY()*10, chart.getAxisLeft()))/10.0)) + " " ,Toast.LENGTH_SHORT)).show();
                    Toast.makeText(Activity_Graph.this,"Date/Time :\t\t" + chart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), chart.getXAxis()) + "\nHumidity :\t" + chart.getAxisLeft().getValueFormatter().getFormattedValue(e.getY(), chart.getAxisLeft()) + " %RH", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }

    }

}
