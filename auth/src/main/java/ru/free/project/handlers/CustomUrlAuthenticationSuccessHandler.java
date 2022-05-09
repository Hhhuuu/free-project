package ru.free.project.handlers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.free.project.exceptions.CommonException;
import ru.free.project.users.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

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
        UserProfileInfoDto user = getUser(authentication);
        setGoodResponseWithBody(response, user);
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

    private UserProfileInfoDto getUser(Authentication authentication) {
        try {
            Optional<UserData> user = userManagerService.findUserByUsername(authentication.getName());
            if (user.isPresent()) {
                UserData userData = user.get();
                return UserProfileInfoDto.builder()
                        .id(userData.getId())
                        .nickname(userData.getNickname())
                        .email(userData.getEmail())
                        .roles(userData.getRoles().stream().map(UserRole::getName).collect(Collectors.toList()))
                        .build();
            }
        } catch (CommonException ignore) {
        }
        return UserProfileInfoDto.builder()
                .id(null)
                .nickname(authentication.getName())
                .email(null)
                .roles(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build();
    }

    public void setRequestCache(final RequestCache requestCache) {
        this.requestCache = requestCache;
    }
}