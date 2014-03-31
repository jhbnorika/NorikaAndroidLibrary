
package com.norika.android.library.utils;

public class HTMLFilter implements Filter {

    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        request.setRequestStr(request.getRequestStr().replace("<", "[")
                .replace(">", "] --------HTMLFilter"));

        chain.doFilter(request, response, chain);

        response.setResponseStr(response.getResponseStr() + "--------HTMLFilter");
    }

}
