package com.haeki.ticTacToeGame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences settings = getSharedPreferences("stats", MODE_PRIVATE);
        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setText(Integer.toString(settings.getInt("gameCount", 0)));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
