package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import com.example.educationalgp.R;

public class GameActivity extends AppCompatActivity {

    private WebView gameWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameWebView = findViewById(R.id.game_webview);
        WebSettings webSettings = gameWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript (if required by the website)

        gameWebView.loadUrl("https://wordwall.net/play/65248/393/585");
        // Enable zooming and scrolling
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false); // Hide the zoom controls
        webSettings.setSupportZoom(true);
        gameWebView.getSettings().setLoadWithOverviewMode(true);
        gameWebView.getSettings().setUseWideViewPort(true);
        gameWebView.setInitialScale(1); // Set initial zoom level
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