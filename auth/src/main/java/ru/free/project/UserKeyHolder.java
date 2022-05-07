package ru.free.project;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import ru.free.project.exceptions.CommonRuntimeException;

import java.util.*;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public class UserKeyHolder {

    private static final String DELIMITER = "|";
    private static final String DELIMITER_TYPE_WITH_DATA = ":";

    enum KeyType {
        ID,
        NICKNAME,
        ANONYMOUS
        ;

        /**
         * @return строковый тип
         */
        public String getType() {
            return this.name();
        }

        /**
         * Получение типа из строки
         *
         * @param type тип
         * @return найденный тип
         */
        public static KeyType fromString(String type) {
            return valueOf(type);
        }
    }

    private final Long id;
    private final String nickname;
    private final boolean isAnonymous;

    private UserKeyHolder(Long id, String nickname, boolean isAnonymous) {
        this.id = id;
        this.nickname = nickname;
        this.isAnonymous = isAnonymous;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public String formatToString() {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        joiner.add(KeyType.ID.getType() + DELIMITER_TYPE_WITH_DATA + id);
        joiner.add(KeyType.NICKNAME.getType() + DELIMITER_TYPE_WITH_DATA + nickname);
        joiner.add(KeyType.ANONYMOUS.getType() + DELIMITER_TYPE_WITH_DATA + isAnonymous);
        return joiner.toString();
    }

    public static UserKeyHolder parseFromString(String usernameData) {
        StringTokenizer tokenizer = new StringTokenizer(usernameData, DELIMITER);
        if (tokenizer.countTokens() != 3) {
            throw new CommonRuntimeException("Неверное количество параметров в username");
        }
        return new UserKeyHolder(
                parseLong(getData(tokenizer.nextToken(), KeyType.ID), KeyType.ID),
                getData(tokenizer.nextToken(), KeyType.NICKNAME),
                BooleanUtils.toBoolean(getData(tokenizer.nextToken(), KeyType.ANONYMOUS))
        );
    }

    public static UserKeyHolder fromIdAndNickname(Long id, String nickname) {
        if (Objects.isNull(id) || id <= 0) {
            throw new CommonRuntimeException("Некорректно задан id");
        }
        if (StringUtils.isBlank(nickname)) {
            throw new CommonRuntimeException("Некорректно задан nickname");
        }
        return new UserKeyHolder(id, nickname, false);
    }

    public static UserKeyHolder forAnonymous(int hashKey, String nickname) {
        if (StringUtils.isBlank(nickname)) {
            throw new CommonRuntimeException("Некорректно задан nickname");
        }
        return new UserKeyHolder(Integer.toUnsignedLong(hashKey), nickname, true);
    }

    private static String getData(String token, KeyType keyType) {
        StringTokenizer tokenData = new StringTokenizer(token, DELIMITER_TYPE_WITH_DATA);
        if (tokenData.countTokens() != 2) {
            throw new CommonRuntimeException("Неверное количество параметров в типе: " + keyType);
        }
        if (KeyType.fromString(tokenData.nextToken()) != keyType) {
            throw new CommonRuntimeException("Некорректный тип: " + keyType);
        }
        return tokenData.nextToken();
    }

    private static Long parseLong(String data, KeyType keyType) {
        try {
            return Long.parseLong(data);
        } catch (NumberFormatException e) {
            throw new CommonRuntimeException("Не удалось распарсить значение: " + keyType);
        }
    }
}
