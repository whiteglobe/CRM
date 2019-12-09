package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ViewCompanyDocument extends AppCompatActivity {

    WebView webViewCompanyDocuments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_company_document);
        getSupportActionBar().hide();

        webViewCompanyDocuments = findViewById(R.id.webViewCompanyDocuments);
        webViewCompanyDocuments.getSettings().setJavaScriptEnabled(true);
        webViewCompanyDocuments.setWebViewClient(new WebViewClient());

        String dataWebView = WebName.imgurl+"radodocs/"+getIntent().getStringExtra("docname");
        webViewCompanyDocuments.loadUrl("https://docs.google.com/viewer?url="+dataWebView);
    }
}
