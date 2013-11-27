
package com.norika.android.library.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 链式核心
 * 
 * @author Norika
 */
public class FilterChain implements Filter {

    private final List<Filter> filters = new ArrayList<Filter>();
    private int index = 0;

    public FilterChain addFilter(Filter f) {
        this.filters.add(f);
        return this;
    }

    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        if (index == filters.size())
            return;

        Filter f = filters.get(index);
        index++;
        f.doFilter(request, response, chain);
    }

}
