package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private gridView Game;
    private Button Easy;
    private Button Medium;
    private Button Hard;
    private Vibrator vibrator;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int HighScore=0;
    private TextView Hsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        Hsc=findViewById(R.id.textView3);
        editor=sharedPreferences.edit();
        HighScore=sharedPreferences.getInt("HSC",0);
        Game=findViewById(R.id.game);
        Easy =findViewById(R.id.Easy);
        Medium=findViewById(R.id.Medium);
        Hard=findViewById(R.id.Hard);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        Game.GetVibrator(vibrator);
        Hsc.setText("HIGHEST SCORE :"+HighScore);
        Easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.NumberOfMines=6;
                Game.setVisibility(View.VISIBLE);
                Easy.setVisibility(View.INVISIBLE);
                Hard.setVisibility(View.INVISIBLE);
                Medium.setVisibility(View.INVISIBLE);
                Hsc.setVisibility(View.INVISIBLE);
                findViewById(R.id.textView4).setVisibility(View.INVISIBLE);
            }
        });
        Hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.NumberOfMines=14;
                Game.setVisibility(View.VISIBLE);
                Easy.setVisibility(View.INVISIBLE);
                Hard.setVisibility(View.INVISIBLE);
                Medium.setVisibility(View.INVISIBLE);
                Hsc.setVisibility(View.INVISIBLE);
                findViewById(R.id.textView4).setVisibility(View.INVISIBLE);
            }
        });
        Medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.NumberOfMines=10;
                Game.setVisibility(View.VISIBLE);
                Easy.setVisibility(View.INVISIBLE);
                Hard.setVisibility(View.INVISIBLE);
                Medium.setVisibility(View.INVISIBLE);
                Hsc.setVisibility(View.INVISIBLE);
                findViewById(R.id.textView4).setVisibility(View.INVISIBLE);
            }
        });
    }
    public void onBackPressed(){
        if(Game.isGameOver){
            if(Game.Score>HighScore){
                editor.putInt("HSC",Game.Score);
                editor.apply();
                HighScore=Game.Score;
                Hsc.setText("HIGHEST SCORE :"+HighScore);
            }
            Game.setVisibility(View.INVISIBLE);
            Easy.setVisibility(View.VISIBLE);
            Hard.setVisibility(View.VISIBLE);
            Medium.setVisibility(View.VISIBLE);
            Hsc.setVisibility(View.VISIBLE);
            findViewById(R.id.textView4).setVisibility(View.VISIBLE);
            Game.endGame();
        }
        else if(!Game.ToStart){
            if(Game.Score>HighScore){
                editor.putInt("HSC",Game.Score);
                editor.apply();
                HighScore=Game.Score;
                Hsc.setText("HIGHEST SCORE :"+HighScore);
            }
            Game.isGameOver=true;
            Game.invalidate();
        }
        else if(Game.getVisibility()==View.VISIBLE){
            if(Game.Score>HighScore){
                editor.putInt("HSC",Game.Score);
                editor.apply();
                HighScore=Game.Score;
                Hsc.setText("HIGHEST SCORE :"+HighScore);
            }
            Game.setVisibility(View.INVISIBLE);
            Easy.setVisibility(View.VISIBLE);
            Hard.setVisibility(View.VISIBLE);
            Medium.setVisibility(View.VISIBLE);
            Hsc.setVisibility(View.VISIBLE);
            findViewById(R.id.textView4).setVisibility(View.VISIBLE);
            Game.endGame();
        }
        else super.onBackPressed();
    }
}