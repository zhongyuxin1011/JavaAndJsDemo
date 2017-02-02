package com.zyx1011.javaandjsdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.button1)
    Button mButton1;
    @Bind(R.id.button2)
    Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        mWebView.addJavascriptInterface(this, "zyx1011");

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://jstojava2")) {
                    Uri uri = Uri.parse(url);
                    String method = uri.getQueryParameter("method");
                    if (method.equals("js1")) {
                        jsToJava1();
                    } else {
                        String param = uri.getQueryParameter("param");
                        jsToJava1(param);
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        mWebView.loadUrl("file:///android_asset/index.html");
    }

    @JavascriptInterface
    public void jsToJava1() {
        Toast.makeText(getApplicationContext(), "Hello World!", Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void jsToJava1(String msg) {
        // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message:");
        builder.setMessage(msg);
        builder.setNegativeButton("sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @OnClick({R.id.button1, R.id.button2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                mWebView.loadUrl("javascript:javaToJs1()");
                break;
            case R.id.button2:
                mWebView.loadUrl("javascript:javaToJs2('Hello JavaScript!')");
                break;
        }
    }
}
