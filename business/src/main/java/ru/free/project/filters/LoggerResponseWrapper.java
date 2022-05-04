package ru.free.project.filters;

import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Обертка над ответом
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public class LoggerResponseWrapper extends ContentCachingResponseWrapper {
    private byte[] cachedContent;

    public LoggerResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    protected void copyBodyToResponse(boolean complete) throws IOException {
        if (complete) {
            this.cachedContent = this.getContentAsByteArray();
        }

        ServletResponse rawResponse = this.getResponse();
        if (!rawResponse.isCommitted()) {
            super.copyBodyToResponse(complete);
        }

    }

    public byte[] getCachedContent() {
        return this.cachedContent;
    }
}
