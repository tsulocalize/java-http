package org.apache.coyote.http11.response;

import org.apache.coyote.http11.request.HttpMimeType;

public class Http11Response {

    private static final String protocol = "HTTP/1.1";

    private String responseBody;
    private Http11ResponseHeaders headers;
    private HttpStatusCode statusCode;
    private String firstLine = "";

    public Http11Response(HttpStatusCode httpStatusCode, String responseBody, Http11ResponseHeaders headers) {
        this.statusCode = httpStatusCode;
        this.responseBody = responseBody;
        this.headers = headers;
    }

    private Http11Response(HttpStatusCode httpStatusCode, String responseBody, String fileExtensions) {
        this(httpStatusCode, responseBody,
                Http11ResponseHeaders.builder()
                        .addHeader("Content-Type", HttpMimeType.from(fileExtensions).asString())
                        .addHeader("Content-Length", String.valueOf(responseBody.getBytes().length))
                        .build());
    }

    public static Http11Response ok() {
        return new Http11Response(HttpStatusCode.OK, "", "");
    }

    public byte[] getBytes() {
        setFirstLine();
        StringBuilder sb = new StringBuilder();
        sb.append(firstLine)
                .append(" \r\n")
                .append(headers.asString())
                .append("\r\n")
                .append(responseBody);

        return sb.toString().getBytes();
    }

    private void setFirstLine() {
        firstLine = String.join(" ",
                protocol,
                String.valueOf(statusCode.getValue()),
                statusCode.getName());
    }

    public void addHeader(String key, String value) {
        headers.addHeader(key, value);
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void setHeaders(Http11ResponseHeaders headers) {
        this.headers = headers;
    }
}
