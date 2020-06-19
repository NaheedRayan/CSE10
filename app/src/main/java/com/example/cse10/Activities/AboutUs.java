package com.example.cse10.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.cse10.R;
import com.example.cse10.Utils;

public class AboutUs extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        //for changing the status bar color
        // Pass on the activity and color resourse
        Utils.darkenStatusBar(this, R.color.LightGreen);

        //hooks
        webView = findViewById(R.id.webView) ;



        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        //for not opening the browser
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://redumbrellacorp.wordpress.com");

    }
}
