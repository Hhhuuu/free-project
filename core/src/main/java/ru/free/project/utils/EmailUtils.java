package ru.free.project.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.free.project.exceptions.CommonException;

import java.util.regex.Pattern;

/**
 * Утилитные методы для email
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@UtilityClass
public class EmailUtils {
    private static final String EMAIL_PATTERN = ".+@.+";
    private static final int MAX_LENGTH = 170;

    /**
     * Является ли строка email
     *
     * @param email строка
     * @return true - да, false - иначе
     */
    public static boolean isEmail(String email) {
        try {
            checkEmail(email);
            return true;
        } catch (CommonException ignore) {
            return false;
        }
    }

    /**
     * Проверка email
     *
     * @param email строка
     * @throws CommonException если проверка не прошла
     */
    public static void checkEmail(String email) throws CommonException {
        if (StringUtils.isBlank(email)) {
            throw new CommonException("Email не может быть пустым");
        }
        if (StringUtils.length(email) > MAX_LENGTH) {
            throw new CommonException(String.format("Email не может быть длиннее %s", MAX_LENGTH));
        }

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        if (!pattern.matcher(email).matches()) {
            throw new CommonException("Email указан неверно. Должен содержать: @");
        }
    }
}
