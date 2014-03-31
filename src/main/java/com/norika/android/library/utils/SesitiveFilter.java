
package com.norika.android.library.utils;

public class SesitiveFilter implements Filter {

    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        request.setRequestStr(request.getRequestStr().replace("敏感", "  ")
                .replace("猫猫", "haha------SesitiveFilter"));

        chain.doFilter(request, response, chain);

        response.setResponseStr(response.getResponseStr() + "------SesitiveFilter");
    }

}
