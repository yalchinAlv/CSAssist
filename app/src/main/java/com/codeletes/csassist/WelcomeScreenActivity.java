package com.codeletes.csassist;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codeletes.csassist.classCodes.CSAssist;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeScreenActivity extends AppCompatActivity {
    final int TIME_OUT = 1000;
    private static CSAssist csAssist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        csAssist =  CSAssist.startCSAssist( getFilesDir());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(WelcomeScreenActivity.this, SectionsMenuActivity.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);


    }

    public static CSAssist getCsAssist() {
        return csAssist;
    }


}
