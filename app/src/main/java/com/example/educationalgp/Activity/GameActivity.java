package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.educationalgp.R;

public class GameActivity extends AppCompatActivity {

    String gameId = "";
    String LESSON_ONE_GAME_URL = "https://wordwall.net/play/65248/393/585";
    String LESSON_TWO_GAME_URL = "https://wordwall.net/ar/embed/eb60bffdc36e4252ab2c8c71a01da040?themeId=2&templateId=25&fontStackId=0";
    String LESSON_THREE_GAME_URL = "https://wordwall.net/embed/32accbb6e7124c12b83b4c0db1b594c3?themeId=26&templateId=49&fontStackId=1";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        WebView gameWebView = findViewById(R.id.game_webview);
        WebSettings webSettings = gameWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript (if required by the website)

        gameId = getIntent().getStringExtra("gameId");

        if (gameId == null) {
            Toast.makeText(this, "لم يتم استرداد معرّف اللعبة", Toast.LENGTH_SHORT).show();
        } else {
            switch (gameId) {
                case "un2less1":
                    gameWebView.loadUrl(LESSON_ONE_GAME_URL);
                    break;
                case "un2less2":
                    gameWebView.loadUrl(LESSON_TWO_GAME_URL);
                    break;
                case "un2less3":
                    gameWebView.loadUrl(LESSON_THREE_GAME_URL);
                    break;
                default:
                    Toast.makeText(this, "غير متوفر حاليا", Toast.LENGTH_SHORT).show();
            }
        }

        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true); // Hide the zoom controls
        webSettings.setSupportZoom(true);
        gameWebView.getSettings().setLoadWithOverviewMode(true);
        gameWebView.getSettings().setUseWideViewPort(true);
        gameWebView.getSettings().setBuiltInZoomControls(true);
        gameWebView.getSettings().setDisplayZoomControls(false);
        gameWebView.getSettings().setSupportZoom(true);

        // Set WebView to fit screen size
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        gameWebView.setLayoutParams(layoutParams);
    }
}