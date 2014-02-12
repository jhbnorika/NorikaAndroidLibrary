
package com.norika.android.library.http;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractCallback<T, K> implements Runnable {
    private String url;
    private Map<String, String> headers;
    private static int NETWORK_POOL = 4;
    private HttpStatus status;

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
        if (status == null)
            status = new HttpStatus();
        else if (status.isDone())
            status.reset();

        work();
    }

    @Override
    public void run() {
        if (status.isDone()) {
            afterwork();
            return;
        }
        if (url == null) {
            status.setCode(HttpStatus.NETWORK_ERROR);
            status.done();
            return;
        }
    }

    private void netRequest() {

    }

    private void work() {
        execute(this);
    }

    private void afterwork() {

    }

    private static ExecutorService fetchExe;

    public static void execute(Runnable job) {
        if (fetchExe == null)
            fetchExe = Executors.newFixedThreadPool(NETWORK_POOL);

        fetchExe.execute(job);
    }

    public static void cancel() {
        if (fetchExe != null) {
            fetchExe.shutdown();
            fetchExe = null;
        }
    }
}
