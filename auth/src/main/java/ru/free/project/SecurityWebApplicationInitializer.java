package ru.free.project;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityWebApplicationInitializer() {
        super(SecurityJavaConfig.class);
    }
}