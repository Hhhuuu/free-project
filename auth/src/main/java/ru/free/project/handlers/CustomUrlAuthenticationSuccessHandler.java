package ru.free.project.handlers;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.free.project.UserKeyHolder;
import ru.free.project.exceptions.CommonException;
import ru.free.project.users.UserData;
import ru.free.project.users.UserManagerService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Component
public class CustomUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements CustomResponseHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private final UserManagerService userManagerService;

    public CustomUrlAuthenticationSuccessHandler(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws ServletException, IOException {
        Optional<UserData> user = getUser(authentication);
        setGoodResponseWithBody(response, user.isPresent() ? user.get() : authentication);
        final SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            clearAuthenticationAttributes(request);
            return;
        }
        final String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            clearAuthenticationAttributes(request);
            return;
        }

        clearAuthenticationAttributes(request);
    }

    private Optional<UserData> getUser(Authentication authentication) {
        try {
            UserKeyHolder holder = UserKeyHolder.parseFromString(authentication.getName());
            return userManagerService.getUserById(holder.getId());
        } catch (CommonException e) {
            return Optional.empty();
        }
    }

    public void setRequestCache(final RequestCache requestCache) {
        this.requestCache = requestCache;
    }
}