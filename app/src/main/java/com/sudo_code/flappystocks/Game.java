package com.sudo_code.flappystocks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        //setContentView(R.layout.activity_game);
        //gameView = (GameView) findViewById(R.id.game_view);
        //gameView.setBackgroundColor(4);
        gameView = new GameView(this.getApplicationContext());
        gameView.setBackgroundColor(0xFFFFFFFF);
        this.setContentView(gameView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //mRunning = false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    @SuppressLint("InlinedApi")
    private void show() {

    }
}
