package com.example.navmixedwithweb;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

/* WebView imports */
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private ActivityResultLauncher<Intent> nav_view_launcher;
    static int web_count = 0;

    public class AndroidBridge {
        @JavascriptInterface
        public String get_string() {
            return "Simple Java String, hello JS!";
        }

        @JavascriptInterface
        public void switch_to_nav(String message) {
            // Run intent to switch to nav view
            Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
            intent.putExtra("message", message);
            nav_view_launcher.launch(intent);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] page_array = {"index.html", "about.html", "digital_clock.html", "switch_to_nav.html"};
        int page_index = getIntent().getIntExtra("page_index", 0);
        Toast.makeText(this, "Page[" + page_index + "]: " + page_array[page_index], Toast.LENGTH_SHORT).show();

        /* Start the web view */
        webView = new WebView(this);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");
        webView.loadUrl("file:///android_asset/" + page_array[page_index]);

        /* handle data returned from nav view */
        nav_view_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result) -> {
            if (result.getResultCode() == MainActivity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    int nav_count = data.getIntExtra("nav_count", 0);
                    Toast.makeText(this, "Activity Result -> Nav Count: " + nav_count, Toast.LENGTH_SHORT).show();
                    /* write in JS */
                    webView.evaluateJavascript("show_response('" + "Activity Result => Nav Count: " + nav_count + "')", null);
                }
            }
        });

        /* handle back press */
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack())
                    webView.goBack();
                else {
                    /* non-root activity return intent (return web count) */
                    final int count = WebViewActivity.web_count++;
                    setResult(RESULT_OK, new Intent().putExtra("web_count", count));
                    Toast.makeText(WebViewActivity.this, "Web_View sets result -> count = " + count, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}