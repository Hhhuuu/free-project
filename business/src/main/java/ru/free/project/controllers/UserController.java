package ru.free.project.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.free.project.AuthenticationAfterRegistration;
import ru.free.project.SecurityContext;
import ru.free.project.exceptions.BusinessException;
import ru.free.project.exceptions.CommonException;
import ru.free.project.users.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@RestController
@RequestMapping(value = "/api/users", headers = {"Accept=application/json"}, produces = {APPLICATION_JSON_VALUE})
public class UserController {

    private final UserRegistrationService userRegistrationService;
    private final UserManagerService userManagerService;
    private final AuthenticationAfterRegistration authenticationAfterRegistration;
    private final SecurityContext securityContext;

    public UserController(UserRegistrationService userRegistrationService,
                          UserManagerService userManagerService,
                          AuthenticationAfterRegistration authenticationAfterRegistration,
                          SecurityContext securityContext) {
        this.userRegistrationService = userRegistrationService;
        this.userManagerService = userManagerService;
        this.authenticationAfterRegistration = authenticationAfterRegistration;
        this.securityContext = securityContext;
    }

    @PostMapping(value = "/registration")
    public UserProfileInfoDto register(@CookieValue(value = "SESSION", required = false) String session, HttpServletRequest request, HttpServletResponse response, @RequestBody(required = true) UserRegistrationRq userRegistrationRq) throws Exception {
        if (Objects.nonNull(session) || securityContext.isAuthenticated()) {
            throw new BusinessException("Необходимо выйти из текущего пользователя", "Для создания нового пользователя необходимо сначала выйти из профиля");
        }

        checkPassword(userRegistrationRq.getPassword(), userRegistrationRq.getRepeatPassword());

        UserRegistrationData userRegistrationData = new UserRegistrationData() {
            @Override
            public String getNickname() {
                return userRegistrationRq.getNickname();
            }

            @Override
            public String getPassword() {
                return userRegistrationRq.getPassword();
            }

            @Override
            public String getEmail() {
                return userRegistrationRq.getEmail();
            }
        };
        UserData userData = userRegistrationService.register(userRegistrationData);

        authenticationAfterRegistration.onAuthentication(request, response, userData, userRegistrationData.getPassword());

        return UserProfileInfoDto.builder()
                .id(userData.getId())
                .nickname(userData.getNickname())
                .email(userData.getEmail())
                .roles(userData.getRoles().stream().map(UserRole::getName).collect(Collectors.toList()))
                .build();
    }

    @PutMapping("/changingPassword")
    public void changePassword(@RequestBody(required = true) ChangingPasswordRq changingPasswordRq) throws CommonException {
        checkPassword(changingPasswordRq.getPassword(), changingPasswordRq.getRepeatPassword());

        UserData user = securityContext.getUser();
        if (user.isAnonymous()) {
            throw new CommonException("Нельзя заменить пароль у анонимного пользователя");
        }

        ChangingPasswordData changingPasswordData = new ChangingPasswordData() {
            @Override
            public String getCurrentPassword() {
                return changingPasswordRq.getOldPassword();
            }

            @Override
            public String getNewPassword() {
                return changingPasswordRq.getPassword();
            }
        };
        userManagerService.changePassword(user, changingPasswordData);
    }

    private void checkPassword(String password, String repeatPassword) throws BusinessException {
        if (!StringUtils.equals(password, repeatPassword)) {
            throw new BusinessException("Пароли не совпадают", "Введенные пароли не совпадают");
        }
    }

}
