package com.haeki.ticTacToeGame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    public static final String SP_GAME_COUNT = "spGameCount";
    public static final String SP_WIN_COUNT = "spWinCount";
    public static final String SP_LOOS_COUNT = "spLoosCount";
    public static final String SP_DRAW_COUNT = "spDrawCount";

    public static final String MP_GAME_COUNT = "mpGameCount";
    public static final String MP_P1_WIN_COUNT = "mpP1WinCount";
    public static final String MP_P2_WIN_COUNT = "mpP2WinCount";
    public static final String MP_DRAW_COUNT = "mpDrawCount";

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = getSharedPreferences("stats", MODE_PRIVATE);
        loadStats();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void loadStats() {
        ((TextView) findViewById(R.id.spGameCountValueText)).setText(Integer.toString(settings.getInt(SP_GAME_COUNT, 0)));
        ((TextView) findViewById(R.id.spWinCountValueText)).setText(Integer.toString(settings.getInt(SP_WIN_COUNT, 0)));
        ((TextView) findViewById(R.id.spLooseCountValueText)).setText(Integer.toString(settings.getInt(SP_LOOS_COUNT, 0)));
        ((TextView) findViewById(R.id.spDrawCountValueText)).setText(Integer.toString(settings.getInt(SP_DRAW_COUNT, 0)));

        ((TextView) findViewById(R.id.mpGameCountValueText)).setText(Integer.toString(settings.getInt(MP_GAME_COUNT, 0)));
        ((TextView) findViewById(R.id.mpWinCountValueText)).setText(Integer.toString(settings.getInt(MP_P1_WIN_COUNT, 0)));
        ((TextView) findViewById(R.id.mpLooseCountValueText)).setText(Integer.toString(settings.getInt(MP_P2_WIN_COUNT, 0)));
        ((TextView) findViewById(R.id.mpDrawCountValueText)).setText(Integer.toString(settings.getInt(MP_DRAW_COUNT, 0)));
    }

    public void resetStats(View view) {
        System.out.println("Resetting Statistics");

        SharedPreferences.Editor editor = settings.edit();

        if(view.getId() == R.id.spResetButton) {
            editor.putInt(SP_GAME_COUNT, 0);
            editor.putInt(SP_WIN_COUNT, 0);
            editor.putInt(SP_LOOS_COUNT, 0);
            editor.putInt(SP_DRAW_COUNT, 0);
        } else if(view.getId() == R.id.mpResetButton) {
            editor.putInt(MP_GAME_COUNT, 0);
            editor.putInt(MP_P1_WIN_COUNT, 0);
            editor.putInt(MP_P2_WIN_COUNT, 0);
            editor.putInt(MP_DRAW_COUNT, 0);
        }

        editor.commit();
        loadStats();
    }

}
