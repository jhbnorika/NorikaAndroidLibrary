
package com.norika.android.library.http;

public class IApi {

    private static final String HOST_URL = "http://fake_url";

    private static final String VERSION;

    private static final String ONLINE_VERSION = "/x.x/";
    private static final String TEST_VERSION = "/x.x_aaaaaa/";

    private static final String ROUTE1 = "route";

    static {
        VERSION = TEST_VERSION;
    }

    public static void httpGet(String url, ChainMap map, ICallback callback) {

    }

}
