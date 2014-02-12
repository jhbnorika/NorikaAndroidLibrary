
package com.norika.android.library.http;

public class HttpStatus {

    public static final int NETWORK_ERROR = -101;
    public static final int AUTH_ERROR = -102;
    public static final int TRANSFORM_ERROR = -103;

    private int code = -1;
    private String status;
    private String error;

    private boolean done;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isDone() {
        return done;
    }

    public HttpStatus done() {
        this.done = true;
        return this;
    }

    public void reset() {
        done = false;
    }
}
