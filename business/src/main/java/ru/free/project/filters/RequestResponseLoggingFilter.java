package ru.free.project.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.free.project.LoggerProperties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Фильтр для логирования запросов/ответов
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Component("requestResponseLoggingFilter")
@Order(1000)
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {
    private static final Set<String> ENTITY_HTTP_METHODS = Set.of("PUT", "POST");

    private final RequestResponseLoggingFilter.MediaTypeAcceptor mediaTypesAcceptor = new RequestResponseLoggingFilter.MediaTypeAcceptor();
    private final LoggerProperties loggerProperties;

    public RequestResponseLoggingFilter(LoggerProperties loggerProperties) {
        this.loggerProperties = loggerProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();

        boolean logRequest = loggerProperties.isLogRequest();
        boolean logHeaders = logRequest && loggerProperties.isLogHeaders();
        long maxBodyLength = loggerProperties.getMaxBodyLength() * 1024;

        LoggerRequestWrapper loggerRequestWrapper = null;
        LoggerResponseWrapper loggerResponseWrapper = null;

        if (logRequest) {
            if (mediaTypesAcceptor.accept(request.getContentType()) && ENTITY_HTTP_METHODS.contains(request.getMethod())) {
                loggerRequestWrapper = new LoggerRequestWrapper(request);
                request = loggerRequestWrapper;
            }
            loggerResponseWrapper = new LoggerResponseWrapper(response);
            response = loggerResponseWrapper;
        }

        Exception error = null;

        try {
            filterChain.doFilter(request, response);
            if (loggerResponseWrapper != null)
                loggerResponseWrapper.copyBodyToResponse();
        } catch (ServletException | IOException | RuntimeException e) {
            error = e;
            throw e;
        } finally {
            if (logRequest) {
                try {
                    String requestLogMessage = requestMessage(request, logHeaders, loggerRequestWrapper, maxBodyLength);
                    String responseLogMessage = responseMessage(response, logHeaders, loggerResponseWrapper, maxBodyLength);
                    logRequestResponse(request.getRequestURI(), requestLogMessage, responseLogMessage, error, System.currentTimeMillis() - start);
                } catch (Exception e) {
                    log.error("Ошибка при логировании запроса/ответа", e);
                }
            }
        }
    }

    public void logRequestResponse(String uri, String requestMessage, String responseMessage, Exception e, long duration) {
        StringBuilder builder = new StringBuilder();
        builder.append(requestMessage);
        builder.append("----------------").append("\n");
        builder.append(responseMessage);
        builder.append("----------------").append("\n");
        builder.append("Длительность запроса: ").append(duration).append(" ms");

        if (e != null) {
            log.error(String.format("Запрос выполнен с ошибкой: %s\n %s", uri, builder), e);
        } else {
            log.info("Получен и отправлен ответ на запрос: {}\n{}", uri, builder);
        }
    }

    private String requestMessage(HttpServletRequest request, boolean withHeaders, LoggerRequestWrapper loggerRequestWrapper, long maxBodyLength) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nMETHOD: ").append(request.getMethod()).append("\n");

        sb.append("URL: ").append(request.getRequestURI());
        if (request.getQueryString() != null) {
            sb.append("?").append(request.getQueryString());
        }
        sb.append("\n");

        if (withHeaders) {
            sb.append("HEADERS: \n");
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (!"Cookie".equalsIgnoreCase(headerName)) {
                    Enumeration<String> headers = request.getHeaders(headerName);
                    while (headers.hasMoreElements()) {
                        sb.append(headerName).append(": ").append(headers.nextElement()).append("\n");
                    }
                }
            }
        }
        if (loggerRequestWrapper != null) {
            byte[] body = loggerRequestWrapper.getContentAsByteArray();
            if (body.length < maxBodyLength)
                sb.append("BODY: \n").append(new String(body));
            else
                sb.append("BODY: \n").append("length = ").append(body.length);
        }
        sb.append("\n");
        return sb.toString();
    }

    private String responseMessage(HttpServletResponse response, boolean withHeaders, LoggerResponseWrapper loggerResponseWrapper, long maxBodyLength) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nSTATUS CODE: ").append(response.getStatus()).append("\n");

        if (withHeaders) {
            sb.append("HEADERS: \n");
            for (String headerName : response.getHeaderNames()) {
                if (!"Set-Cookie".equalsIgnoreCase(headerName)) {
                    for (String headerVal : response.getHeaders(headerName)) {
                        sb.append(headerName).append(": ").append(headerVal).append("\n");
                    }
                }
            }
        }
        if (loggerResponseWrapper != null && mediaTypesAcceptor.accept(response.getContentType())) {
            byte[] body = loggerResponseWrapper.getCachedContent();
            if (body != null) {
                if (body.length < maxBodyLength)
                    sb.append("BODY: \n").append(new String(body));
                else
                    sb.append("BODY length = ").append(body.length).append("\n");
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    public static class MediaTypeAcceptor {
        private final Map<String, Set<String>> acceptedTypes = Map.of(
                "application", Set.of("json")
        );


        public boolean accept(String contentType) {
            if (contentType != null) {
                try {
                    MediaType mediaType = MediaType.parseMediaType(contentType);
                    Set<String> subtypes = acceptedTypes.get(mediaType.getType());
                    if (subtypes != null)
                        return subtypes.contains(mediaType.getSubtype());
                } catch (Exception ignore) {
                }//ignore
            }
            return false;
        }
    }
}
