package com.example.server.interceptor;

import com.example.server.accessors.accessorsImpl.*;
import com.example.server.context.CustomContext;
import com.example.server.context.DetachedRequestAttributes;
import com.example.server.model.ContextParamDefault;
import com.example.server.model.CustomContextParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UnifiedContextInterceptor implements HandlerInterceptor {

    @Autowired
    private HeaderContextAccessor headerContextAccessor;

    @Autowired
    private CustomContextAccessor customContextAccessor;

    @Autowired
    private MdcAccessor mdcAccessor;

    @Autowired
    private RequestAttributesAccessor requestAttributesAccessor;

    @Autowired
    private LocaleContextAccessor localeContextAccessor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Capture headers
        Map<String, String> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));

        headerContextAccessor.setValue(headers);

        // Explicitly capture and store RequestAttributes as a detached copy
        org.springframework.web.context.request.RequestAttributes requestAttributes = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            DetachedRequestAttributes detached = new DetachedRequestAttributes((ServletRequestAttributes) requestAttributes, headers);
            requestAttributesAccessor.setValue(detached);
            System.out.println("Captured DetachedRequestAttributes in interceptor");
        } else if (requestAttributes != null) {
            requestAttributesAccessor.setValue(requestAttributes);
            System.out.println("Captured original RequestAttributes in interceptor: " + requestAttributes.getClass().getSimpleName());
        } else {
            System.out.println("No RequestAttributes available in interceptor");
        }

        // Explicitly capture and store LocaleContext
        LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
        if (localeContext != null) {
            localeContextAccessor.setValue(localeContext);
            System.out.println("Captured LocaleContext in interceptor: " + localeContext.getLocale());
        } else {
            System.out.println("No LocaleContext available in interceptor");
        }

        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        if (mdcContext != null) {
            mdcAccessor.setValue(mdcContext);
        }

        // Explicitly capture and store custom contexts thread local
        if (!CustomContext.isInitialized()) {
            CustomContext.init();
            CustomContext.putObject(ContextParamDefault.X_TRAFFIC_COLOR, "blue");
            CustomContext.putObject(ContextParamDefault.X_TRAFFIC_TYPE, "live");
            customContextAccessor.setValue(CustomContext.getCopyOfContextHolderMap());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        headerContextAccessor.clear();
        mdcAccessor.clear();
    }
}
