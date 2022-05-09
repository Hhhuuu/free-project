package ru.free.project.users.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.free.project.exceptions.BusinessException;
import ru.free.project.exceptions.CommonException;
import ru.free.project.users.UserRegistrationData;
import ru.free.project.utils.*;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@UtilityClass
@Slf4j
public class UserUtils {

    public static void checkPassword(String rawPassword) throws BusinessException {
        try {
            PasswordUtils.checkPassword(rawPassword, PasswordUtils.PasswordType.USER);
        } catch (CommonException e) {
            log.error("Password некорректный", e);
            throw new BusinessException("Некорректно заполнен password", e.getMessage());
        }
    }

    public static void checkEmail(String email) throws BusinessException {
        try {
            EmailUtils.checkEmail(email);
        } catch (CommonException e) {
            log.error("Email некорректный", e);
            throw new BusinessException("Некорректно заполнен email", e.getMessage());
        }
    }

    public static void checkNickname(UserRegistrationData registrationData) throws BusinessException {
        try {
            NicknameUtils.checkNickname(registrationData.getNickname());
        } catch (CommonException e) {
            log.error("Nickname некорректный", e);
            throw new BusinessException("Некорректно заполнен nickname", e.getMessage());
        }
    }
}
