package ru.free.project.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.free.project.exceptions.CommonException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Утилитный метод для пароля
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@UtilityClass
public class PasswordUtils {
    public enum PasswordType {
        USER("^[A-Za-z0-9!@#$%^&*()_+{}|№%:,.;-=]+$", 8, 25),
        ;

        private final String pattern;
        private final Integer minLength;
        private final Integer maxLength;

        PasswordType(String pattern, int minLength, int maxLength) {
            this.pattern = pattern;
            this.minLength = minLength;
            this.maxLength = maxLength;
        }

        public String getPattern() {
            return pattern;
        }

        public Integer getMinLength() {
            return minLength;
        }

        public Integer getMaxLength() {
            return maxLength;
        }
    }

    /**
     * Проверка пароля на корректность
     *
     * @param password     пароль
     * @param passwordType тип пароля
     */
    public static void checkPassword(String password, PasswordType passwordType) throws CommonException {
        Objects.requireNonNull(passwordType, "Тип пароля не может быть null");
        if (StringUtils.isBlank(password)) {
            throw new CommonException("Пароль не может быть пустым");
        }

        if (Objects.nonNull(passwordType.getMinLength()) && password.length() < passwordType.getMinLength()) {
            throw new CommonException(String.format("Пароль должен быть короче %s символов", passwordType.getMinLength()));
        }

        if (Objects.nonNull(passwordType.getMaxLength()) && password.length() > passwordType.getMaxLength()) {
            throw new CommonException(String.format("Пароль должен быть длиннее %s символов", passwordType.getMaxLength()));
        }

        Pattern pattern = Pattern.compile(passwordType.getPattern());
        if (!pattern.matcher(password).matches()) {
            throw new CommonException("Пароль должен содержать следующие символы: " + pattern.pattern());
        }
    }
}
