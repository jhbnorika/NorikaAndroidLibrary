
package com.norika.android.library.http;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractCallback<T, K> implements Runnable {
    private String url;
    private Map<String, String> headers;
    private static int NETWORK_POOL = 4;

    private K self() {
        return (K) this;
    }

    public K url(String url) {
        this.url = url;
        return self();
    }

    public K header(String key, String value) {
        if (headers == null)
            headers = new HashMap<String, String>();
        headers.put(key, value);
        return self();
    }

    private void clear() {
        url = null;
        headers = null;
    }

    public void async() {
        work();
    }

    @Override
    public void run() {

    }

    private void work() {
        execute(this);
    }

    private static ExecutorService fetchExe;

    public static void execute(Runnable job) {
        if (fetchExe == null)
            fetchExe = Executors.newFixedThreadPool(NETWORK_POOL);

        fetchExe.execute(job);
    }
}
