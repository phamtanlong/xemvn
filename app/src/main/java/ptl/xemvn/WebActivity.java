package ptl.xemvn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WebActivity extends Activity {

    private WebView webView;
    private ProgressDialog pd;
    private static String jsCode;

    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);

        loadJsCode(WebActivity.this);

        pd = new ProgressDialog(WebActivity.this);
        pd.setMessage("Đang tải...");
        pd.show();

        webView = findViewById(R.id.webview1);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                cleanView();
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                if (!pd.isShowing()) {
                    pd.show();
                }
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, String url) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }

                cleanView();
            }
        });

        webView.loadUrl(getIntent().getStringExtra("url"));
    }

    public static void loadJsCode (Context context) {
        if (!TextUtils.isEmpty(jsCode))
            return;

        InputStream inputStream = context.getResources().openRawResource(R.raw.clean_webview);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            jsCode = "";
            String line = reader.readLine();
            while (line != null) {
                jsCode += line + "\n";
                line = reader.readLine();
            }
            Log.i("JsCode", "JsCode load done!");
            Log.i("JsCode", jsCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cleanView () {
        Log.i("WebView", "Clean View");
        webView.loadUrl("javascript:" + jsCode);
    }
}
