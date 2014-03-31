
package com.norika.android.library.http;

import java.util.HashMap;
import java.util.Map;

public abstract class HttpCallback<T> {
    private String url;
    private final Map<String, String> headers = new HashMap<String, String>();

    public void url(String url) {
        this.url = url;
    }

    public void header(String key, String value) {
        headers.put(key, value);
    }

}
