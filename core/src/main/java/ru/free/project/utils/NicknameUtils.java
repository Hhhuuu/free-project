package ru.free.project.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.free.project.exceptions.CommonException;

import java.util.regex.Pattern;

/**
 * Утилитные методы для nickname
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@UtilityClass
public class NicknameUtils {
    private static final String NICKNAME_PATTERN = "[A-Za-z0-9_]+";
    private static final String NICKNAME_INCORRECT_PATTERN_ONLY_UNDERLINING = "[_]+";
    private static final String NICKNAME_INCORRECT_PATTERN_ONLY_NUMBER = "[0-9]+";
    private static final int MAX_LENGTH = 70;
    private static final int MIN_LENGTH = 3;

    /**
     * Является ли строка nickname
     *
     * @param nickname строка
     * @return true - да, false - иначе
     */
    public static boolean isNickname(String nickname) {
        try {
            checkNickname(nickname);
            return true;
        } catch (CommonException ignore) {
            return false;
        }
    }

    /**
     * Проверка nickname
     *
     * @param nickname строка
     * @throws CommonException если проверка не прошла
     */
    public static void checkNickname(String nickname) throws CommonException {
        if (StringUtils.isBlank(nickname)) {
            throw new CommonException("Nickname не может быть пустым");
        }
        if (StringUtils.length(nickname) < MIN_LENGTH && StringUtils.length(nickname) > MAX_LENGTH) {
            throw new CommonException(String.format("Nickname не может быть короче %s и длиннее %s", MIN_LENGTH, MAX_LENGTH));
        }

        if (checkIncorrectNickname(nickname, NICKNAME_INCORRECT_PATTERN_ONLY_NUMBER)) {
            throw new CommonException("Nickname не может состоять только из цифр");
        }

        if (checkIncorrectNickname(nickname, NICKNAME_INCORRECT_PATTERN_ONLY_UNDERLINING)) {
            throw new CommonException("Nickname не может состоять только из знаков \"_\"");
        }

        Pattern pattern = Pattern.compile(NICKNAME_PATTERN);
        if (!pattern.matcher(nickname).matches()) {
            throw new CommonException("Nickname должен состоять из символов латинского алфавита, допускаются знаки подчеркивания и цифры");
        }
    }

    private static boolean checkIncorrectNickname(String nickname, String patternSrc) {
        Pattern pattern = Pattern.compile(patternSrc);
        return pattern.matcher(nickname).matches();
    }
}
