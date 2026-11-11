package au.com.easybooks.approvals;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Easy Books Approvals: a full-screen app that runs the Approvals web app.
 * Phone numbers open the dialler, email links open the mail app, and
 * OneDrive / other websites open in their own apps.
 */
public class MainActivity extends Activity {

    private static final String APP_URL = "https://obradmilovic1.github.io/approvals/";
    private static final String APP_HOST = "obradmilovic1.github.io";
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);      // remembers the passcode on this phone
        s.setDatabaseEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String scheme = uri.getScheme() == null ? "" : uri.getScheme();
                boolean inApp = ("https".equals(scheme) || "http".equals(scheme)) && APP_HOST.equals(uri.getHost());
                if (inApp) return false;
                try {
                    Intent i = "tel".equals(scheme) ? new Intent(Intent.ACTION_DIAL, uri)
                            : "mailto".equals(scheme) ? new Intent(Intent.ACTION_SENDTO, uri)
                            : new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                } catch (Exception ignored) { }
                return true;
            }
        });

        if (savedInstanceState != null) web.restoreState(savedInstanceState);
        else web.loadUrl(APP_URL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        web.saveState(outState);
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }
}
