package ru.free.project.users;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.free.project.*;
import ru.free.project.exceptions.BusinessException;
import ru.free.project.exceptions.CommonException;
import ru.free.project.utils.*;

import java.util.*;

import static ru.free.project.utils.PasswordUtils.*;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Service("userRegistrationServiceImpl")
@Slf4j
public class UserRegistrationServiceImpl implements UserRegistrationService {
    private static final String DEFAULT_USER_ROLE = "USER";

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    @Override
    public UserData register(UserRegistrationData registrationData) throws CommonException {
        checkNickname(registrationData);
        checkPassword(registrationData);

        Optional<String> formattedEmail = Optional.ofNullable(StringUtils.defaultIfBlank(registrationData.getEmail(), null));
        if (formattedEmail.isPresent()) {
            checkEmail(registrationData);
            formattedEmail.map(String::toLowerCase);
        }

        Role role = getDefaultRole();

        User user = new User();
        user.setNickname(registrationData.getNickname());
        formattedEmail.ifPresent(user::setEmail);
        user.setPassword(passwordEncoder.encode(registrationData.getPassword()));
        userRepository.save(user);

        ru.free.project.UserRole userRole = new ru.free.project.UserRole();
        userRole.setRoleId(role.getId());
        userRole.setUserId(user.getId());
        userRoleRepository.save(userRole);

        return new UserDataDto(user, Collections.singletonList(new UserRoleDto(role)));
    }

    private void checkEmail(UserRegistrationData registrationData) throws BusinessException {
        try {
            EmailUtils.checkEmail(registrationData.getEmail());
        } catch (CommonException e) {
            log.error("Email некорректный", e);
            throw new BusinessException("Некорректно заполнен email", e.getMessage());
        }
    }

    private void checkPassword(UserRegistrationData registrationData) throws BusinessException {
        try {
            PasswordUtils.checkPassword(registrationData.getPassword(), PasswordType.USER);
        } catch (CommonException e) {
            log.error("Password некорректный", e);
            throw new BusinessException("Некорректно заполнен password", e.getMessage());
        }
    }

    private void checkNickname(UserRegistrationData registrationData) throws BusinessException {
        try {
            NicknameUtils.checkNickname(registrationData.getNickname());
        } catch (CommonException e) {
            log.error("Nickname некорректный", e);
            throw new BusinessException("Некорректно заполнен nickname", e.getMessage());
        }
    }

    private Role getDefaultRole() throws CommonException {
        Optional<Role> role = roleRepository.findByName(DEFAULT_USER_ROLE);
        if (role.isEmpty()) {
            throw new CommonException("Не найдена роль: " + DEFAULT_USER_ROLE);
        }
        return role.get();
    }
}
