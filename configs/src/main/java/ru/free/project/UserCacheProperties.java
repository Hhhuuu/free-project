package ru.free.project;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.Duration;

/**
 * Настройки кэша пользовательских данных
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Configuration
@PropertySource("classpath:userCache.properties")
@ConfigurationProperties(prefix = "users.cache")
public class UserCacheProperties {

    private Duration expireAfterWrite;
    private Long maxSize;
    private boolean enabled;

    public Duration getExpireAfterWrite() {
        return expireAfterWrite;
    }

    public void setExpireAfterWrite(Duration expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}