// Copyright (C) 2015 The Qt Company Ltd.
// SPDX-License-Identifier: LicenseRef-Qt-Commercial OR LGPL-3.0-only OR GPL-2.0-only OR GPL-3.0-only

package org.qtproject.example.mapview;

import android.content.pm.PackageManager;
import android.view.View;
import android.webkit.GeolocationPermissions;
//import com.google.android.libraries.maps.MapView;
//import android.webkit.URLUtil;
//import android.webkit.ValueCallback;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.webkit.WebChromeClient;
//import android.webkit.CookieManager;
//import com.google.android.gms.maps.MapView;
import java.lang.Runnable;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import java.lang.String;
//import android.webkit.WebSettings;
import android.util.Log;
//import android.webkit.WebSettings.PluginState;
import android.graphics.Bitmap;
import java.util.concurrent.Semaphore;
import java.lang.reflect.Method;
import android.os.Build;
import java.util.concurrent.TimeUnit;
import java.time.format.DateTimeFormatter;
import android.view.Window;

public class MapViewController
{
    private final Activity m_activity;
    private final long m_id;
    private boolean m_hasLocationPermission;
//    private WebView m_webView = null;
//    private MapView m_mapView = null;
    private MapViewWrapper m_mapView = null;
    private static final String TAG = "MapViewController";
    private final int INIT_STATE = 0;
    private final int STARTED_STATE = 1;
    private final int LOADING_STATE = 2;
    private final int FINISHED_STATE = 3;

    private volatile int m_loadingState = INIT_STATE;
    private volatile int m_progress = 0;
    private volatile int m_frameCount = 0;

    // API 11 methods
//    private Method m_webViewOnResume = null;
//    private Method m_webViewOnPause = null;
//    private Method m_webSettingsSetDisplayZoomControls = null;

    /*// API 19 methods
    private Method m_webViewEvaluateJavascript = null;

    // Native callbacks
    private native void c_onPageFinished(long id, String url);
    private native void c_onPageStarted(long id, String url, Bitmap icon);
    private native void c_onProgressChanged(long id, int newProgress);
    private native void c_onReceivedIcon(long id, Bitmap icon);
    private native void c_onReceivedTitle(long id, String title);
    private native void c_onRunJavaScriptResult(long id, long callbackId, String result);
    private native void c_onReceivedError(long id, int errorCode, String description, String url);
    private native void c_onCookieAdded(long id, boolean result, String domain, String name);
    private native void c_onCookieRemoved(long id, boolean result, String domain, String name);*/

    // We need to block the UI thread in some cases, if it takes to long we should timeout before
    // ANR kicks in... Usually the hard limit is set to 10s and if exceed that then we're in trouble.
    // In general we should not let input events be delayed for more then 500ms (If we're spending more
    // then 200ms somethings off...).
    private final long BLOCKING_TIMEOUT = 250;

    private void resetLoadingState(final int state)
    {
        m_progress = 0;
        m_frameCount = 0;
        m_loadingState = state;
    }

    public MapViewController(final Activity activity, final long id)
    {
        m_activity = activity;
        m_id = id;
        final Semaphore sem = new Semaphore(0);
        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("BEFORE", "BEFORE");
                m_mapView = new MapViewWrapper(m_activity);
                m_mapView.createMapView();
                Log.d("AFTER", "AFTER");
//                m_mapView = new MapView(m_activity);
//                m_hasLocationPermission = hasLocationPermission(m_mapView);
//                WebSettings webSettings = m_webView.getSettings();

//                // The local storage options are not user changeable in QtWebView and disabled by default on Android.
//                // In QtWebEngine and on iOS local storage is enabled by default, so we follow that.
//                webSettings.setDatabaseEnabled(true);
//                webSettings.setDomStorageEnabled(true);

                if (Build.VERSION.SDK_INT > 10) {
                    try {
//                        m_webViewOnResume = m_webView.getClass().getMethod("onResume");
//                        m_webViewOnPause = m_webView.getClass().getMethod("onPause");
//                        m_webSettingsSetDisplayZoomControls = webSettings.getClass().getMethod("setDisplayZoomControls", boolean.class);
//                        if (Build.VERSION.SDK_INT > 18) {
//                            m_webViewEvaluateJavascript = m_webView.getClass().getMethod("evaluateJavascript",
//                                                                                         String.class,
//                                                                                         ValueCallback.class);
//                        }
                    } catch (Exception e) { // Do nothing
                        e.printStackTrace();
                    }
                }

//                //allowing access to location without actual ACCESS_FINE_LOCATION may throw security exception
//                webSettings.setGeolocationEnabled(m_hasLocationPermission);

//                webSettings.setJavaScriptEnabled(true);
//                if (m_webSettingsSetDisplayZoomControls != null) {
//                    try { m_webSettingsSetDisplayZoomControls.invoke(webSettings, false); } catch (Exception e) { e.printStackTrace(); }
//                }
//                webSettings.setBuiltInZoomControls(true);
//                webSettings.setPluginState(PluginState.ON);
//                m_webView.setWebViewClient((WebViewClient)new QtAndroidWebViewClient());
//                m_webView.setWebChromeClient((WebChromeClient)new QtAndroidWebChromeClient());
                sem.release();
            }
        });

        try {
            sem.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void stopLoading()
    {
        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() { m_webView.stopLoading(); }
        });
    }

    public void reload()
    {
        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() { m_webView.reload(); }
        });
    }

    public String getTitle()
    {
        final String[] title = {""};
        final Semaphore sem = new Semaphore(0);
        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() { title[0] = m_webView.getTitle(); sem.release(); }
        });

        try {
            sem.tryAcquire(BLOCKING_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return title[0];
    }

    public int getProgress()
    {
        return m_progress;
    }

    public boolean isLoading()
    {
        return m_loadingState == LOADING_STATE || m_loadingState == STARTED_STATE || (m_progress > 0 && m_progress < 100);
    }*/

//    public MapView getMapView() {
//        return m_mapView;
//    }

    public MapViewWrapper getView() {
        return m_mapView;
    }

    public Window getWindow() {
        return m_mapView.getWindow();
    }

    /*public void onPause()
    {
        if (m_webViewOnPause == null)
            return;

        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() { try { m_webViewOnPause.invoke(m_webView); } catch (Exception e) { e.printStackTrace(); } }
        });
    }

    public void onResume()
    {
        if (m_webViewOnResume == null)
            return;

        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() { try { m_webViewOnResume.invoke(m_webView); } catch (Exception e) { e.printStackTrace(); } }
        });
    }*/

    private static boolean hasLocationPermission(View view)
    {
        final String name = view.getContext().getPackageName();
        final PackageManager pm = view.getContext().getPackageManager();
        return pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", name) == PackageManager.PERMISSION_GRANTED;
    }

    /*public void destroy()
    {
        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_webView.destroy();
            }
        });
    }*/
}
