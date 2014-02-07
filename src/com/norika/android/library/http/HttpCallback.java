
package com.norika.android.library.http;

public abstract class HttpCallback<T> {
    public static final int HTTP_500 = 500;
    public static final int HTTP_200 = 200;
    public static final int HTTP_404 = 404;

    public HttpCallback() {

    }

    public final void callback(String url, T object, HttpStatus status) {
        onComplete(url, object);
        switch (status.getCode()) {
            case HttpStatus.TRANSFORM_ERROR:
                onTransformError(url, status);
                break;
            case HTTP_404:
                on404Error(url, status);
                break;
            case HTTP_500:
                on500Error(url, status);
                break;
            case HTTP_200:
                on200Success(url, object);
                break;
            case HttpStatus.NETWORK_ERROR:
                onNetError(url, status);
                break;

            default:
                break;
        }
    }

    protected void onTransformError(String url, HttpStatus status) {

    }

    protected void onNetError(String url, HttpStatus status) {

    }

    protected void on404Error(String url, HttpStatus status) {

    }

    protected void on500Error(String url, HttpStatus status) {

    }

    protected abstract void on200Success(String url, T object);

    protected void onComplete(String url, T object) {

    }
}
