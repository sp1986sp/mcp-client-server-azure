package com.example.server.accessors.accessorsImpl;

import com.example.server.accessors.ThreadLocalAccessor;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LocaleContextAccessor implements ThreadLocalAccessor<LocaleContext> {

    private static final InheritableThreadLocal<LocaleContext> localeContextHolder = new InheritableThreadLocal<>();

    @Override
    public String getKey() {
        return "locale-context";
    }

    @Override
    public LocaleContext getValue() {
        LocaleContext stored = localeContextHolder.get();
        if (stored != null) {
            return stored;
        }
        return LocaleContextHolder.getLocaleContext();
    }

    @Override
    public void setValue(LocaleContext value) {
        localeContextHolder.set(value);
        if (value != null) {
            LocaleContextHolder.setLocaleContext(value);
        }
    }

    @Override
    public void clear() {
        localeContextHolder.remove();
        LocaleContextHolder.resetLocaleContext();
    }
}
