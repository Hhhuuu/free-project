package ru.free.project.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Component
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl implements CustomResponseHandler {

    public static final String ACCESS_DENIED_TEXT = "Доступ запрещён!";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (!response.isCommitted()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            setFailResponse(response, accessDeniedException, ACCESS_DENIED_TEXT);
        }
    }
}
