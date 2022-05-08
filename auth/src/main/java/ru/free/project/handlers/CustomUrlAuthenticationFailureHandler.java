package ru.free.project.handlers;

 import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Component
public class CustomUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler implements CustomResponseHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (LockedException.class.isAssignableFrom(exception.getClass())) {
            setFailResponse(response, exception, "Извините, аккаунт заблокирован");
        } else if (DisabledException.class.isAssignableFrom(exception.getClass())) {
            setFailResponse(response, exception, "Извините, аккаунт отключен");
        } else if (AccountExpiredException.class.isAssignableFrom(exception.getClass())) {
            setFailResponse(response, exception, "Извините, у аккаунта истекло время действия");
        } else {
            setFailResponse(response, exception, "Введен некорректный логин или пароль.");
        }
    }
}