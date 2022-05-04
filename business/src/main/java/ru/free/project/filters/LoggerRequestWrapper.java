package ru.free.project.filters;

import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;

/**
 * Обертка над запросом
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public class LoggerRequestWrapper extends ContentCachingRequestWrapper {
    public LoggerRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public byte[] getContentAsByteArray() {
        return super.getContentAsByteArray();
    }
}
