package ru.free.project;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.free.project.handlers.CustomResponseHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Component
public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint, CustomResponseHandler {

    private static final String UNAUTHORIZED_TEXT = "Для продолжения работы необходимо авторизироваться";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        setFailResponse(response, authException, UNAUTHORIZED_TEXT);
    }
}