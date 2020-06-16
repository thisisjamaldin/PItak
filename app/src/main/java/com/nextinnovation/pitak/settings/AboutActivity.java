package com.nextinnovation.pitak.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nextinnovation.pitak.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        WebView wv = findViewById(R.id.pdf_about);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl("http://docs.google.com/gview?embedded=true&url=https://firebasestorage.googleapis.com/v0/b/pitak-37337.appspot.com/o/pitak%20%D0%BE%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5.pdf?alt=media&token=0517a897-926b-46cb-8191-d0c63167e9a4");
    }
}