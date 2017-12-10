package ptl.xemvn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebActivity extends Activity {

    private WebView webView;
//    private ProgressBar progressBar;
    private ProgressDialog pd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);

        //progressBar = findViewById(R.id.web_progress);

        pd = new ProgressDialog(WebActivity.this);
        pd.setMessage("Đang tải...");
        pd.show();

        webView = findViewById(R.id.webview1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra("url"));

//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                Log.i("Progress", "" + newProgress);
//                progressBar.setProgress(newProgress);
//            }
//        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                if (!pd.isShowing()) {
                    pd.show();
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
    }
}
