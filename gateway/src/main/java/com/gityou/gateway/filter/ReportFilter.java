package com.gityou.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpResponse;
import org.springframework.stereotype.Component;


@Component
public class ReportFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 600;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        RibbonApacheHttpResponse response = (RibbonApacheHttpResponse) requestContext.get("ribbonResponse");
        if (response.getRequestedURI().getPath().equals("/file/list")) {
            if (response.getRequestedURI().getPort() == 7072)
                System.out.println("实例: 7072");
            else if (response.getRequestedURI().getPort() == 7073)
                System.out.println("实例: 7073");
        }
        return null;
    }
}
