package com.onlineexamvzm25.nmmssalur;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.io.OutputStream;

public class MainActivity extends Activity {
    private static final String APP_URL = "https://onlineexamvzm25.github.io/nmmssalur/";
    private WebView webView;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); hideSystemBars();
        webView = new WebView(this); webView.setBackgroundColor(Color.WHITE); setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true); webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true); webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false); webView.getSettings().setSupportMultipleWindows(false);
        webView.addJavascriptInterface(new DownloadBridge(), "AndroidDownload"); webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri=request.getUrl(); String host=uri.getHost()==null?"":uri.getHost();
                if ("onlineexamvzm25.github.io".equalsIgnoreCase(host) || "lxkemkvtcwbnkaarcgns.supabase.co".equalsIgnoreCase(host) || "cdn.jsdelivr.net".equalsIgnoreCase(host)) return false;
                if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) { startActivity(new Intent(Intent.ACTION_VIEW,uri)); return true; }
                return false;
            }
            @Override public void onPageFinished(WebView view,String url) { super.onPageFinished(view,url); installBlobDownloadHandler(); }
        });
        if(savedInstanceState!=null) webView.restoreState(savedInstanceState); else webView.loadUrl(APP_URL);
    }
    private void installBlobDownloadHandler() {
        String js="(function(){if(window.__androidBlobHandlerInstalled)return;window.__androidBlobHandlerInstalled=true;var original=URL.createObjectURL.bind(URL),blobs={};URL.createObjectURL=function(b){var u=original(b);blobs[u]=b;return u;};document.addEventListener('click',function(e){var a=e.target.closest&&e.target.closest('a');if(!a||!a.href||!a.href.startsWith('blob:')||!blobs[a.href])return;e.preventDefault();e.stopImmediatePropagation();var r=new FileReader();r.onloadend=function(){var s=String(r.result||''),p=s.indexOf(',');if(p>=0&&window.AndroidDownload)AndroidDownload.saveBase64(a.download||'exam-result.pdf',s.substring(p+1));};r.readAsDataURL(blobs[a.href]);},true);})();";
        webView.evaluateJavascript(js,null);
    }
    private class DownloadBridge { @JavascriptInterface public void saveBase64(String filename,String base64){ runOnUiThread(()->savePdf(filename,base64)); } }
    private void savePdf(String filename,String base64) {
        try { byte[] bytes=Base64.decode(base64,Base64.DEFAULT); String name=(filename==null||filename.trim().isEmpty())?"exam-result.pdf":filename.replaceAll("[^a-zA-Z0-9._-]","_"); if(!name.toLowerCase().endsWith(".pdf"))name+=".pdf";
            ContentValues values=new ContentValues(); values.put(MediaStore.Downloads.DISPLAY_NAME,name); values.put(MediaStore.Downloads.MIME_TYPE,"application/pdf");
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)values.put(MediaStore.Downloads.RELATIVE_PATH,Environment.DIRECTORY_DOWNLOADS);
            Uri uri=getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI,values); if(uri==null)throw new Exception("Could not create download file");
            try(OutputStream out=getContentResolver().openOutputStream(uri)){if(out==null)throw new Exception("Could not open download file");out.write(bytes);} Toast.makeText(this,"PDF saved to Downloads: "+name,Toast.LENGTH_LONG).show();
        }catch(Exception e){Toast.makeText(this,"PDF download failed. Please try again.",Toast.LENGTH_LONG).show();}
    }
    private void hideSystemBars(){getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);}
    @Override public void onWindowFocusChanged(boolean hasFocus){super.onWindowFocusChanged(hasFocus);if(hasFocus)hideSystemBars();}
    @Override protected void onSaveInstanceState(Bundle outState){super.onSaveInstanceState(outState);if(webView!=null)webView.saveState(outState);}
    @Override public void onBackPressed(){if(webView!=null&&webView.canGoBack())webView.goBack();else super.onBackPressed();}
    @Override protected void onDestroy(){if(webView!=null){webView.removeJavascriptInterface("AndroidDownload");webView.destroy();}super.onDestroy();}
}
