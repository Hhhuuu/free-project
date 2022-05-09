package ru.free.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.*;
import ru.free.project.exceptions.CommonException;
import ru.free.project.users.*;

import java.util.Optional;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Slf4j
public class DatabaseUserDetailsService implements UserDetailsService {
    private static final String USER_NOT_FOUND = "Пользователь отсутствует";
    private final UserManagerService userManagerService;
    private final SecurityContext securityContext;

    public DatabaseUserDetailsService(UserManagerService userManagerService,
                                      SecurityContext securityContext) {
        this.userManagerService = userManagerService;
        this.securityContext = securityContext;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserData userData = getUser(username);
            securityContext.putIfNotPresent(userData);
            return User.builder()
                    .username(username)
                    .password(userData.getPassword())
                    .roles(userData.getRoles().stream()
                            .map(UserRole::getName)
                            .toArray(String[]::new)
                    )
                    .build();
        } catch (CommonException e) {
            log.error("Не задан nickname", e);
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }
    }

    private UserData getUser(final String username) throws CommonException {
        Optional<UserData> userData = userManagerService.findUserByUsername(username);
        if (userData.isEmpty()) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }
        return userData.get();
    }
}
