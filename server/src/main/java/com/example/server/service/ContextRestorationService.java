package com.example.server.service;

import com.example.server.accessors.accessorsImpl.*;
import com.example.server.context.CustomContext;
import com.example.server.model.CustomContextParam;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ContextRestorationService {

    private final HeaderContextAccessor headerContextAccessor;
    private final CustomContextAccessor customContextAccessor;
    private final MdcAccessor mdcAccessor;
    private final RequestAttributesAccessor requestAttributesAccessor;
    private final LocaleContextAccessor localeContextAccessor;

    @Autowired
    public ContextRestorationService(HeaderContextAccessor headerContextAccessor,
                                     CustomContextAccessor customContextAccessor,
                                     MdcAccessor mdcAccessor,
                                     RequestAttributesAccessor requestAttributesAccessor,
                                     LocaleContextAccessor localeContextAccessor) {
        this.headerContextAccessor = headerContextAccessor;
        this.customContextAccessor = customContextAccessor;
        this.mdcAccessor = mdcAccessor;
        this.requestAttributesAccessor = requestAttributesAccessor;
        this.localeContextAccessor = localeContextAccessor;
    }

    /**
     * Restores all ThreadLocal contexts and returns HTTP headers
     *
     * @return HttpHeaders populated with request headers
     */
    public HttpHeaders restoreAllContextsAndGetHeaders() {
        restoreCustomContext();
        restoreMDCContext();
        restoreRequestAttributes();
        restoreLocaleContext();
        return getHttpHeaders();
    }

    /**
     * Restores all ThreadLocal contexts without returning headers
     */
    public void restoreAllContexts() {
        restoreCustomContext();
        restoreMDCContext();
        restoreRequestAttributes();
        restoreLocaleContext();
    }

    /**
     * Gets HTTP headers without restoring other contexts
     *
     * @return HttpHeaders populated with request headers
     */
    public HttpHeaders getHttpHeaders() {
        Map<String, String> headers = headerContextAccessor.getValue();
        System.out.println("Headers in Tool: " + headers);

        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::add);
        }
        return httpHeaders;
    }

    // Restoring Custom Thread Local in Tools
    private void restoreCustomContext() {
        Map<CustomContextParam, Object> customContext = customContextAccessor.getValue();
        if (!CustomContext.isInitialized() && customContext != null) {
            try {
                CustomContext.setContextHolderMap(customContext); // Removed as method is undefined
                System.out.println("CustomContext would be restored here if supported");
            } catch (Exception e) {
                System.err.println("Failed to restore CustomContext: " + e.getMessage());
            }
        }
    }

    private void restoreMDCContext() {
        Map<String, String> mdcContext = mdcAccessor.getValue();
        System.out.println("MDC context available: " + (mdcContext != null && !mdcContext.isEmpty()));
        if (mdcContext != null) {
            MDC.setContextMap(mdcContext);
        }
    }

    private void restoreRequestAttributes() {
        org.springframework.web.context.request.RequestAttributes requestAttributes = requestAttributesAccessor.getValue();
        System.out.println("RequestAttributes available: " + (requestAttributes != null));
        if (requestAttributes != null) {
            try {
                org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(requestAttributes);
                System.out.println("RequestAttributes restored successfully");
            } catch (Exception e) {
                System.err.println("Failed to restore RequestAttributes: " + e.getMessage());
            }
        }
    }

    private void restoreLocaleContext() {
        LocaleContext localeContext = localeContextAccessor.getValue();
        System.out.println("LocaleContext available: " + (localeContext != null));

        if (localeContext != null) {
            try {
                LocaleContextHolder.setLocaleContext(localeContext);
                System.out.println("LocaleContext restored successfully");
            } catch (Exception e) {
                System.err.println("Failed to restore LocaleContext: " + e.getMessage());
            }
        }
    }
}