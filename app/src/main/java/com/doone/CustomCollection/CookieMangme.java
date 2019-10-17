package com.doone.CustomCollection;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieMangme implements CookieJar {
    private CookieManager cookieManager;

    public CookieMangme(Context context) {
        CookieSyncManager.createInstance(context).sync();
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        for (okhttp3.Cookie ck : cookies) {
            cookieManager.setCookie(url.host(), ck.toString());
        }
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            cookieManager.flush();
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        cookieManager.removeExpiredCookie();//删除过时
        String cookie = cookieManager.getCookie(url.toString());
        ArrayList<Cookie> res = new ArrayList<>();
        if (cookie != null) {
            for (String value : cookie.split(";")){
                res.add(Cookie.parse(url, value));
            }
        }
        return res;
    }
    public String getCookieString(String url){
        return cookieManager.getCookie(url);
    }

    public void creaCookie(){
        cookieManager.removeSessionCookie();// 移除
        cookieManager.removeAllCookie();
    }
}