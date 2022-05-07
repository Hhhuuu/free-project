package ru.free.project;

import com.google.common.cache.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.free.project.exceptions.CommonException;
import ru.free.project.exceptions.CommonRuntimeException;
import ru.free.project.users.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Configuration
public class SecurityContextConfiguration {

    @Slf4j
    private static class SecurityContextImpl implements SecurityContext {
        private static final UserData ANONYMOUS = new UserData() {
            @Override
            public boolean isAnonymous() {
                return true;
            }

            @Override
            public Long getId() {
                throw new CommonRuntimeException("У анонимного пользователя нет ID");
            }

            @Override
            public String getEmail() {
                throw new CommonRuntimeException("У анонимного пользователя нет email");
            }

            @Override
            public String getNickname() {
                return ANONYMOUS_USER;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public List<UserRole> getRoles() {
                return Collections.emptyList();
            }
        };

        private static final String ANONYMOUS_USER = "anonymousUser";
        private final Cache<String, UserData> users;
        private final UserManagerService userManagerService;

        public SecurityContextImpl(UserManagerService userManagerService) {
            this.users = CacheBuilder.newBuilder()
                    .expireAfterWrite(10L, TimeUnit.MINUTES)
                    .maximumSize(10000L)
                    .build();
            this.userManagerService = userManagerService;
        }

        @Override
        public UserData getUser() throws CommonException {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            try {
                UserKeyHolder userKey = getUserKey(authentication);
                return users.get(userKey.formatToString(), () -> getUser(userKey));
            } catch (Exception e) {
                log.error("Проблема при получении данных о пользователе", e);
                throw new CommonException("Не удалось получить информацию о пользователе", e);
            }
        }

        private UserKeyHolder getUserKey(Authentication authentication) {
            UserKeyHolder userKey;
            if (AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
                userKey = UserKeyHolder.forAnonymous(((AnonymousAuthenticationToken) authentication).getKeyHash(), authentication.getName());
            } else {
                userKey = UserKeyHolder.parseFromString(authentication.getName());
            }
            return userKey;
        }

        @Override
        public void putIfNotPresent(UserData userData) {
            if (userData.isAnonymous()) {
                return;
            }
            users.put(UserKeyHolder.fromIdAndNickname(userData.getId(), userData.getNickname()).formatToString(), userData);
        }

        private UserData getUser(UserKeyHolder userKey) {
            try {
                if (userKey.isAnonymous()) {
                    return ANONYMOUS;
                }

                Optional<UserData> user = userManagerService.getUserById(userKey.getId());
                return user.orElse(ANONYMOUS);
            } catch (CommonException e) {
                log.error("Не удалось получить информацию о пользователе", e);
                return ANONYMOUS;
            }
        }
    }

    @Bean
    public SecurityContext securityContext(UserManagerService userManagerService) {
        return new SecurityContextImpl(userManagerService);
    }
}
