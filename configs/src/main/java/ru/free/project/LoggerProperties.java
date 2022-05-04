package ru.free.project;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки для логирования запросов/ответов
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@ConfigurationProperties(prefix = "logger")
public class LoggerProperties {

    private boolean logRequest;
    private boolean logHeaders;
    private long maxBodyLength;

    /**
     * @return разрешено ли логирование запроса/ответа
     */
    public boolean isLogRequest() {
        return logRequest;
    }

    public void setLogRequest(boolean logRequest) {
        this.logRequest = logRequest;
    }

    /**
     * @return разрешено ли логирование заголовков
     */
    public boolean isLogHeaders() {
        return logHeaders;
    }

    public void setLogHeaders(boolean logHeaders) {
        this.logHeaders = logHeaders;
    }

    /**
     * @return максимальный размер сообщения
     */
    public long getMaxBodyLength() {
        return maxBodyLength;
    }

    public void setMaxBodyLength(long maxBodyLength) {
        this.maxBodyLength = maxBodyLength;
    }
}
