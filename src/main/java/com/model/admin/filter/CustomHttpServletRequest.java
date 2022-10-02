package com.model.admin.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * packageName : com.model.admin.filter
 * className : CustomHttpServletRequest
 * user : jwlee
 * date : 2022/10/02
 */
public class CustomHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String,String> customHeaders;

    public CustomHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
    }

    public void putHeader(String name, String value){
        this.customHeaders.put(name, value);
    }

    public String getHeader(String name) {
        // check the custom headers first
        String headerValue = customHeaders.get(name);

        if (headerValue != null){
            return headerValue;
        }
        // else return from into the original wrapped object
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

}
