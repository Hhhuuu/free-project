package ru.free.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;

import javax.servlet.http.HttpSession;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Configuration
public class AuthenticationConfiguration {

    @Bean("authenticationAfterRegistration")
    public AuthenticationAfterRegistration authenticationAfterRegistration() {
        return (request, response, userData, password) -> {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userData.getNickname(), password);
            authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            ChangeSessionIdAuthenticationStrategy authenticationStrategy = new ChangeSessionIdAuthenticationStrategy();
            authenticationStrategy.setAlwaysCreateSession(true);
            authenticationStrategy.onAuthentication(authRequest, request, response);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authRequest);
            SecurityContextHolder.setContext(context);

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        };
    }
}
