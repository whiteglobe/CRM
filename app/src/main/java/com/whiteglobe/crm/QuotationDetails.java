package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class QuotationDetails extends AppCompatActivity {

    WebView webViewQuotationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_details);
        getSupportActionBar().hide();

        webViewQuotationDetails = findViewById(R.id.webViewQuotationDetails);
        webViewQuotationDetails.getSettings().setJavaScriptEnabled(true);
        webViewQuotationDetails.setWebViewClient(new WebViewClient());

        String dataWebView = WebName.weburl + "viewquotation.php?qt=" + getIntent().getStringExtra("quotno");
        webViewQuotationDetails.loadUrl(dataWebView);
    }
}
