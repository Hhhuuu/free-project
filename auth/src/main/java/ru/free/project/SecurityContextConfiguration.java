package ru.free.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Configuration
public class SecurityContextConfiguration {

    @Bean
    public SecurityContext securityContext() {
        return () -> SecurityContextHolder.getContext().getAuthentication();
    }
}
