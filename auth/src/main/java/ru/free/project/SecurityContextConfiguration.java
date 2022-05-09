package ru.free.project;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import ru.free.project.exceptions.CommonException;
import ru.free.project.exceptions.CommonRuntimeException;
import ru.free.project.users.*;

import java.util.*;
import java.util.concurrent.Callable;

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
        private final UserManagerService userManagerService;
        private final UserCacheProperties userCacheProperties;
        private Cache<String, UserData> users;
        

        public SecurityContextImpl(UserManagerService userManagerService,
                                   UserCacheProperties userCacheProperties) {
            if (userCacheProperties.isEnabled()) {
                initializeCache(userCacheProperties);
            }
            this.userManagerService = userManagerService;
            this.userCacheProperties = userCacheProperties;
        }

        private void initializeCache(UserCacheProperties userCacheProperties) {
            Assert.isTrue(userCacheProperties.getMaxSize() > 0, "Максимальный размер кэша должен быть больше 0");
            Assert.isTrue(Objects.nonNull(userCacheProperties.getExpireAfterWrite()) && !userCacheProperties.getExpireAfterWrite().isZero() && !userCacheProperties.getExpireAfterWrite().isNegative(), "Время жизни не должно быть отрицательным");

            this.users = CacheBuilder.newBuilder()
                    .expireAfterWrite(userCacheProperties.getExpireAfterWrite())
                    .maximumSize(userCacheProperties.getMaxSize())
                    .build();
        }

        @Override
        public UserData getUser() throws CommonException {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            try {
                if (isAnonymous(authentication)) {
                    return ANONYMOUS;
                }
                Callable<UserData> userDataCallable = () -> getUser(authentication.getName()) ;
                return getUserDataAndPutToCacheIfNeed(authentication.getName(), userDataCallable);
            } catch (Exception e) {
                log.error("Проблема при получении данных о пользователе", e);
                throw new CommonException("Не удалось получить информацию о пользователе", e);
            }
        }

        private boolean isAnonymous(Authentication authentication) {
            return AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass());
        }

        @Override
        public boolean isAuthenticated() {
            return !isAnonymous(SecurityContextHolder.getContext().getAuthentication());
        }

        private UserData getUserDataAndPutToCacheIfNeed(String userKey, Callable<UserData> userDataCallable) throws Exception {
            if (!userCacheProperties.isEnabled()) {
                return userDataCallable.call();
            }
            return users.get(userKey, userDataCallable);
        }

        @Override
        public void putIfNotPresent(UserData userData) {
            if (!userCacheProperties.isEnabled()) {
                log.warn("Кэш пользователей выключен");
                return;
            }

            if (userData.isAnonymous()) {
                return;
            }

            users.put(Objects.requireNonNull(userData.getNickname()), userData);
        }

        private UserData getUser(String username) {
            try {
                Optional<UserData> user = userManagerService.findUserByUsername(username);
                return user.orElse(ANONYMOUS);
            } catch (CommonException e) {
                log.error("Не удалось получить информацию о пользователе", e);
                return ANONYMOUS;
            }
        }
    }

    @Bean
    public SecurityContext securityContext(UserManagerService userManagerService,
                                           UserCacheProperties userCacheProperties) {
        return new SecurityContextImpl(userManagerService, userCacheProperties);
    }
}
