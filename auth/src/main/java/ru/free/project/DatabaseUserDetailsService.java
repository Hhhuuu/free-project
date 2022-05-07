package ru.free.project;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
                    .username(UserKeyHolder.fromIdAndNickname(userData.getId(), userData.getNickname()).formatToString())
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
        String formattedUsername = checkAndLowerCaseUsername(username);
        Optional<UserData> userData = isNickname(formattedUsername) ? userManagerService.getUserByNickname(formattedUsername) : userManagerService.getUserByEmail(formattedUsername);
        if (userData.isEmpty()) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }
        return userData.get();
    }

    private String checkAndLowerCaseUsername(final String username) {
        String result = StringUtils.lowerCase(username);
        if (StringUtils.isBlank(result)) {
            log.error("Не задан username");
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }
        return result;
    }

    private boolean isNickname(final String username) {
        return !username.contains("@");
    }
}
