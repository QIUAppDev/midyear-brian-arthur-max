/*
* A test class for implementing the Graph package
* comes with an Activity that displays a graph
* */

package com.example.brian.subwaytime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;

public class GraphTest extends AppCompatActivity {

    LineChart chart; //corresponds with XML entry

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_test);

        chart = (LineChart) findViewById(R.id.chart);

        //TODO: implement adding data
    }
}
