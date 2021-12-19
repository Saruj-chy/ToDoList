package com.sd.spartan.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private boolean action = false ;
    private int SPLASH_TIME = 3000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!action) {
                    goActivity();
                }
            }
        }, SPLASH_TIME);



    }

    private void goActivity() {
        Intent mySuperIntent = new Intent(this, HomeActivity.class);
        startActivity(mySuperIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPLASH_TIME = 0;
        action = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        SPLASH_TIME = 0;
        action = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SPLASH_TIME = 0;
        action = true;
        finish();
    }
}