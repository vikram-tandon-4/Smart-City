package hackuta.vikramtandonapps.com.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import hackuta.vikramtandonapps.com.R;
import hackuta.vikramtandonapps.com.models.WebDataModel;


public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private Toolbar toolbar;
    private TextView toolBarHeader;
    private WebView webView;
    private String URL = "url";
    private WebDataModel data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_webview);
        initializeViews();
    }

    private void initializeViews() {

        data = (WebDataModel) getIntent().getSerializableExtra(URL);
        /*
         *
         * initializing toolbar
         *
         * */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarHeader = (TextView) findViewById(R.id.toolBarHeader);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setUserAgentString("Android WebView");
//
        toolBarHeader.setText(data.getTitle());
        webView.loadUrl(data.getUrl());

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.e("data:::", url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                Log.e("data:::", url);
            }
        });
    }

}
