package com.example.server.context;

import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Creating Mock RequestAttributes, so that it can be accessed in a Async Thread.
 */
public class DetachedRequestAttributes extends ServletRequestAttributes {
    private final Map<String, Object> attributeMap = new HashMap<>();
    private final String sessionId;

    public DetachedRequestAttributes(ServletRequestAttributes original, Map<String, String> headers) {
        super(new MinimalHttpServletRequest(headers), new MinimalHttpServletResponse());
        this.sessionId = original.getSessionId();

        // Copy request attributes (if you use them)
        for (String name : original.getAttributeNames(SCOPE_REQUEST)) {
            attributeMap.put(name, original.getAttribute(name, SCOPE_REQUEST));
        }
    }

    @Override
    public Object getAttribute(String name, int scope) {
        return scope == SCOPE_REQUEST ? attributeMap.get(name.toLowerCase()) : null;
    }

    @Override
    public void setAttribute(String name, Object value, int scope) {
        if (scope == SCOPE_REQUEST) attributeMap.put(name.toLowerCase(), value);
    }

    @Override
    public void removeAttribute(String name, int scope) {
        if (scope == SCOPE_REQUEST) attributeMap.remove(name.toLowerCase());
    }

    @Override
    public String[] getAttributeNames(int scope) {
        return scope == SCOPE_REQUEST ? attributeMap.keySet().toArray(new String[0]) : new String[0];
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    // --- Minimal Dummy Request Implementation ---
    private static class MinimalHttpServletRequest implements jakarta.servlet.http.HttpServletRequest {
        private final Map<String, String> headers;

        MinimalHttpServletRequest(Map<String, String> headers) {
            this.headers = headers != null ? new HashMap<>(headers) : new HashMap<>();
        }

        @Override
        public String getHeader(String name) {
            return headers.get(name.toLowerCase());
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            return Collections.enumeration(headers.keySet());
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            String value = headers.get(name.toLowerCase());
            return value != null ? Collections.enumeration(Collections.singletonList(value)) : Collections.emptyEnumeration();
        }

        @Override
        public int getIntHeader(String name) {
            return -1;
        }

        @Override
        public long getDateHeader(String name) {
            return -1L;
        }

        @Override
        public jakarta.servlet.http.Cookie[] getCookies() {
            return new jakarta.servlet.http.Cookie[0];
        }

        @Override
        public String getAuthType() {
            return null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return new HashMap<>();
        }

        @Override
        public String getProtocol() {
            return "HTTP/1.1";
        }

        @Override
        public String getScheme() {
            return "http";
        }

        @Override
        public String getServerName() {
            return "localhost";
        }

        @Override
        public int getServerPort() {
            return 8080;
        }

        @Override
        public String getRemoteAddr() {
            return "127.0.0.1";
        }

        @Override
        public String getRemoteHost() {
            return "localhost";
        }

        @Override
        public void setAttribute(String name, Object o) {
        }

        @Override
        public void removeAttribute(String name) {
        }

        @Override
        public java.util.Locale getLocale() {
            return java.util.Locale.getDefault();
        }

        @Override
        public Enumeration<java.util.Locale> getLocales() {
            return Collections.emptyEnumeration();
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public jakarta.servlet.RequestDispatcher getRequestDispatcher(String path) {
            return null;
        }

        @Override
        public int getRemotePort() {
            return 0;
        }

        @Override
        public String getLocalName() {
            return "localhost";
        }

        @Override
        public String getLocalAddr() {
            return "127.0.0.1";
        }

        @Override
        public int getLocalPort() {
            return 8080;
        }

        @Override
        public jakarta.servlet.ServletContext getServletContext() {
            return null;
        }

        @Override
        public jakarta.servlet.AsyncContext startAsync() throws IllegalStateException {
            return null;
        }

        @Override
        public jakarta.servlet.AsyncContext startAsync(jakarta.servlet.ServletRequest servletRequest, jakarta.servlet.ServletResponse servletResponse) throws IllegalStateException {
            return null;
        }

        @Override
        public boolean isAsyncStarted() {
            return false;
        }

        @Override
        public boolean isAsyncSupported() {
            return false;
        }

        @Override
        public jakarta.servlet.AsyncContext getAsyncContext() {
            return null;
        }

        @Override
        public jakarta.servlet.DispatcherType getDispatcherType() {
            return jakarta.servlet.DispatcherType.REQUEST;
        }

        @Override
        public String getParameter(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.emptyEnumeration();
        }

        @Override
        public String[] getParameterValues(String name) {
            return null;
        }

        @Override
        public Object getAttribute(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return Collections.emptyEnumeration();
        }

        @Override
        public String getCharacterEncoding() {
            return "UTF-8";
        }

        @Override
        public void setCharacterEncoding(String env) throws java.io.UnsupportedEncodingException {
        }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public long getContentLengthLong() {
            return 0;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public jakarta.servlet.ServletInputStream getInputStream() throws java.io.IOException {
            return new jakarta.servlet.ServletInputStream() {
                private final java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(new byte[0]);

                @Override
                public int read() throws java.io.IOException {
                    return bais.read();
                }

                @Override
                public boolean isFinished() {
                    return true;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(jakarta.servlet.ReadListener readListener) {
                }
            };
        }

        @Override
        public java.io.BufferedReader getReader() throws java.io.IOException {
            return null;
        }

        @Override
        public String getRemoteUser() {
            return null;
        }

        @Override
        public boolean isUserInRole(String role) {
            return false;
        }

        @Override
        public java.security.Principal getUserPrincipal() {
            return null;
        }

        @Override
        public String getRequestedSessionId() {
            return null;
        }

        @Override
        public StringBuffer getRequestURL() {
            return new StringBuffer();
        }

        @Override
        public String getServletPath() {
            return "";
        }

        @Override
        public jakarta.servlet.http.HttpSession getSession(boolean create) {
            return null;
        }

        @Override
        public jakarta.servlet.http.HttpSession getSession() {
            return null;
        }

        @Override
        public String changeSessionId() {
            return null;
        }

        @Override
        public boolean isRequestedSessionIdValid() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromCookie() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromURL() {
            return false;
        }

        @Override
        public boolean authenticate(jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException, jakarta.servlet.ServletException {
            return false;
        }

        @Override
        public void login(String username, String password) throws jakarta.servlet.ServletException {
        }

        @Override
        public void logout() throws jakarta.servlet.ServletException {
        }

        @Override
        public java.util.Collection<jakarta.servlet.http.Part> getParts() throws java.io.IOException, jakarta.servlet.ServletException {
            return new java.util.ArrayList<>();
        }

        @Override
        public jakarta.servlet.http.Part getPart(String name) throws java.io.IOException, jakarta.servlet.ServletException {
            return null;
        }

        @Override
        public <T extends jakarta.servlet.http.HttpUpgradeHandler> T upgrade(Class<T> handlerClass) {
            return null;
        }

        @Override
        public String getMethod() {
            return "GET";
        }

        @Override
        public String getRequestURI() {
            return "/";
        }

        @Override
        public String getProtocolRequestId() {
            return null;
        }

        @Override
        public String getPathTranslated() {
            return null;
        }

        @Override
        public String getPathInfo() {
            return null;
        }

        @Override
        public String getQueryString() {
            return null;
        }

        @Override
        public jakarta.servlet.ServletConnection getServletConnection() {
            return null;
        }

        @Override
        public String getContextPath() {
            return "";
        }

        @Override
        public String getRequestId() {
            return null;
        }
    }

    // --- Minimal Dummy Response Implementation ---
    private static class MinimalHttpServletResponse implements jakarta.servlet.http.HttpServletResponse {
        @Override
        public void addCookie(jakarta.servlet.http.Cookie cookie) {
        }

        @Override
        public boolean containsHeader(String name) {
            return false;
        }

        @Override
        public String encodeURL(String url) {
            return url;
        }

        @Override
        public String encodeRedirectURL(String url) {
            return url;
        }

        public String encodeUrl(String url) {
            return url;
        }

        public String encodeRedirectUrl(String url) {
            return url;
        }

        @Override
        public void sendError(int sc, String msg) throws java.io.IOException {
        }

        @Override
        public void sendError(int sc) throws java.io.IOException {
        }

        @Override
        public void sendRedirect(String location) throws java.io.IOException {
        }

        @Override
        public void setDateHeader(String name, long date) {
        }

        @Override
        public void addDateHeader(String name, long date) {
        }

        @Override
        public void setHeader(String name, String value) {
        }

        @Override
        public void addHeader(String name, String value) {
        }

        @Override
        public void setIntHeader(String name, int value) {
        }

        @Override
        public void addIntHeader(String name, int value) {
        }

        public void setStatus(int sc, String sm) {
        }

        @Override
        public int getStatus() {
            return 200;
        }

        @Override
        public void setStatus(int sc) {
        }

        @Override
        public String getHeader(String name) {
            return null;
        }

        @Override
        public java.util.Collection<String> getHeaders(String name) {
            return new java.util.ArrayList<>();
        }

        @Override
        public java.util.Collection<String> getHeaderNames() {
            return new java.util.ArrayList<>();
        }

        @Override
        public String getCharacterEncoding() {
            return "UTF-8";
        }

        @Override
        public void setCharacterEncoding(String charset) {
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public void setContentType(String type) {
        }

        @Override
        public jakarta.servlet.ServletOutputStream getOutputStream() throws java.io.IOException {
            return null;
        }

        @Override
        public java.io.PrintWriter getWriter() throws java.io.IOException {
            return null;
        }

        @Override
        public void setContentLength(int len) {
        }

        @Override
        public void setContentLengthLong(long len) {
        }

        @Override
        public int getBufferSize() {
            return 0;
        }

        @Override
        public void setBufferSize(int size) {
        }

        @Override
        public void flushBuffer() throws java.io.IOException {
        }

        @Override
        public void resetBuffer() {
        }

        @Override
        public boolean isCommitted() {
            return false;
        }

        @Override
        public void reset() {
        }

        @Override
        public java.util.Locale getLocale() {
            return java.util.Locale.getDefault();
        }

        @Override
        public void setLocale(java.util.Locale loc) {
        }
    }
}