package com.code.common.crawl;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

import java.net.URI;

public class WebRedirectHandler extends LaxRedirectStrategy {
    @Override
    public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
        URI uri = this.getLocationURI(request, response, context);
        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("HEAD")) {
            return new HttpHead(uri);
        } else if (method.equalsIgnoreCase("GET")) {
            return new HttpGet(uri);
        } else {
            int status = response.getStatusLine().getStatusCode();
            HttpUriRequest req = null;
            req = status == 307 ? RequestBuilder.copy(request).setUri(uri).build() : new HttpGet(uri);
//            req.addHeader(new BasicHeader("Cookie", ));
            return req;
        }
    }

//    private String cookie
}
