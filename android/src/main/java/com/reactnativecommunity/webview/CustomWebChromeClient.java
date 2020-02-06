package com.reactnativecommunity.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Message;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class CustomWebChromeClient extends WebChromeClient {
	Activity myActivity;
	WebView childView;
  RNCWebViewManager.RNCWebView myWebView;
  static AlertDialog builder;

	public CustomWebChromeClient(Activity activity, RNCWebViewManager.RNCWebView webView) {
		myActivity = activity;
    myWebView = webView;
	}

	protected ViewGroup getRootView() {
		return (ViewGroup) myActivity.findViewById(android.R.id.content);
	}

	@Override
	public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
		childView = new WebView(myActivity);
		WebSettings settings = childView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setSupportMultipleWindows(true);
		settings.setUseWideViewPort(false);
		childView.setWebChromeClient(new CustomWebChromeClient(myActivity, null));
		childView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
        System.out.println(url);
      }
		});

    childView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    builder = new AlertDialog.Builder(myActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen).create();

    builder.setTitle("");
    builder.setView(childView);

    builder.show();
    builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);


    // tell the transport about the new view
		WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
		transport.setWebView(childView);
		resultMsg.sendToTarget();
		return true;
	}

	@Override
	public void onCloseWindow(WebView window) {
    try {
      builder.dismiss();
    } catch (Exception e) {
      System.out.println(e);
    }
		getRootView().removeView(window);
    window.destroy();

    super.onCloseWindow(window);
		System.out.println("close");
	}
}
