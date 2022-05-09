package ru.free.project.users;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.free.project.*;
import ru.free.project.exceptions.CommonException;
import ru.free.project.utils.EmailUtils;
import ru.free.project.utils.NicknameUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Service("userManagerServiceImpl")
public class UserManagerServiceImpl implements UserManagerService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserManagerServiceImpl(UserRepository userRepository,
                                  UserRoleRepository userRoleRepository,
                                  RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public Optional<UserData> getUserById(Long id) throws CommonException {
        if (Objects.isNull(id) || id <= 0) {
            throw new CommonException("Не задан id");
        }

        Optional<User> userData = userRepository.findById(id);
        if (userData.isEmpty()) {
            return Optional.empty();
        }

        return buildUserData(userData.get());
    }

    @Transactional
    @Override
    public Optional<UserData> getUserByNickname(String nickname) throws CommonException {
        if (StringUtils.isBlank(nickname)) {
            throw new CommonException("Не задан nickname");
        }

        Optional<User> userData = userRepository.getUserByNickname(nickname);
        if (userData.isEmpty()) {
            return Optional.empty();
        }

        return buildUserData(userData.get());

    }

    @Transactional
    @Override
    public Optional<UserData> getUserByEmail(String email) throws CommonException {
        if (StringUtils.isBlank(email)) {
            throw new CommonException("Не задан email");
        }

        Optional<User> userData = userRepository.getUserByEmail(email);
        if (userData.isEmpty()) {
            return Optional.empty();
        }

        return buildUserData(userData.get());
    }

    @Override
    public Optional<UserData> findUserByUsername(String username) throws CommonException {
        if (StringUtils.isBlank(username)) {
            throw new CommonException("Не задан username");
        }

        String formattedUsername = StringUtils.lowerCase(username);
        if (NicknameUtils.isNickname(formattedUsername)) {
            return getUserByNickname(formattedUsername);
        } else if (EmailUtils.isEmail(formattedUsername)) {
            return getUserByEmail(formattedUsername);
        }
        return Optional.empty();
    }

    private Optional<UserData> buildUserData(User user) {
        List<UserRole> roles = getRoles(user.getId());
        return Optional.of(new UserDataDto(user, roles));
    }

    private List<UserRole> getRoles(Long userId) {
        List<Long> rolesIds = userRoleRepository.getUserRolesByUserId(userId).stream()
                .map(ru.free.project.UserRole::getRoleId)
                .collect(Collectors.toList());

        return ((List<Role>) roleRepository.findAllById(rolesIds))
                .stream()
                .map(UserRoleDto::new)
                .collect(Collectors.toList());
    }

}
