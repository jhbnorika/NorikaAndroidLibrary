
package com.norika.android.library.utils;

public class FaceFilter implements Filter {

    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        request.setRequestStr(request.getRequestStr().replace(":)",
                "^V^-------FaceFilter"));

        chain.doFilter(request, response, chain);

        response.setResponseStr(response.getResponseStr() + "-------FaceFilter");
    }

}
