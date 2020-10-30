package hr.fitbit.demo.fitbitconnect.service;

import hr.fitbit.demo.fitbitconnect.apimodel.user.User;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserRegister;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserResponse;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserUpdate;
import hr.fitbit.demo.fitbitconnect.entity.RoleEntity;
import hr.fitbit.demo.fitbitconnect.entity.UserEntity;
import hr.fitbit.demo.fitbitconnect.repository.RoleRepository;
import hr.fitbit.demo.fitbitconnect.repository.UserRepository;
import hr.fitbit.demo.fitbitconnect.util.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Page<User> getUsers(String username, Pageable pageRequest) {
        return userRepository.findByUsernameContaining(username, pageRequest).map(this::convertUserEntityToUserModel);
    }

    @Transactional
    public Optional<User> getUser(UUID userId) {
        final Optional<UserEntity> entity = userRepository.findByUserId(userId);
        if (entity.isPresent()) {
            return Optional.of(convertUserEntityToUserModel(entity.get()));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public UserResponse registerUser(UserRegister userRegister) {
        final UserEntity userEntity = convertUserRegisterModelToUserEntity(userRegister);
        userRepository.save(userEntity);

        final Collection<RoleEntity> roles = assignRolesAccordingToUserType(userRegister.getType());
        roles.forEach(role -> {
            role.addUser(userEntity);
            roleRepository.save(role);
        });

        userEntity.addAllRoles(roles);
        userRepository.save(userEntity);

        final UserResponse userResponse = new UserResponse();
        userResponse.setUserId(userEntity.getUserId());
        userResponse.setUsername(userEntity.getUsername());
        userResponse.setType(userEntity.getType());
        return userResponse;
    }

    @Transactional
    public boolean updateUser(UUID userId, UserUpdate userUpdate) {
        final Optional<UserEntity> entity = userRepository.findByUserId(userId);

        if (entity.isPresent()) {
            final UserEntity userEntity = entity.get();

            if (userUpdate.getPassword() != null) {
                userEntity.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
            }

            if (userUpdate.getType() != null) {
                userEntity.setType(userUpdate.getType());
                userRepository.save(userEntity);

                userEntity.getRoles().forEach(role -> {
                    role.getUsers().remove(userEntity);
                    roleRepository.save(role);
                });

                final Collection<RoleEntity> roles = assignRolesAccordingToUserType(userUpdate.getType());
                roles.forEach(role -> {
                    role.addUser(userEntity);
                    roleRepository.save(role);
                });

                userEntity.setRoles(roles);
                userRepository.save(userEntity);
            }

            if (userUpdate.getName() != null) {
                userEntity.setName(userUpdate.getName());
            }

            if (userUpdate.getLastName() != null) {
                userEntity.setLastName(userUpdate.getLastName());
            }

            if (userUpdate.getAge() != null) {
                userEntity.setAge(userUpdate.getAge());
            }

            userRepository.save(userEntity);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean deleteUser(UUID userId) {
        final Optional<UserEntity> entity = userRepository.findByUserId(userId);
        if (entity.isEmpty()) {
            return false;
        }

        final UserEntity userEntity = entity.get();
        userEntity.getRoles().forEach(role -> {
            role.getUsers().remove(userEntity);
            roleRepository.save(role);
        });
        userRepository.delete(userEntity);
        return true;
    }

    private User convertUserEntityToUserModel(UserEntity entity) {

        final User user = new User();
        user.setUserId(entity.getUserId());
        user.setUsername(entity.getUsername());
        user.setType(entity.getType());
        user.setName(entity.getName());
        user.setLastName(entity.getLastName());
        user.setEmail(entity.getEmail());
        user.setAge(entity.getAge());
        return user;
    }

    private UserEntity convertUserRegisterModelToUserEntity(UserRegister userRegister) {

        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegister.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        userEntity.setEmail(userRegister.getEmail());
        userEntity.setType(userRegister.getType());
        if (userRegister.getName() != null) {
            userEntity.setName(userRegister.getName());
        }
        if (userRegister.getLastName() != null) {
            userEntity.setLastName(userRegister.getLastName());
        }
        if (userRegister.getAge() != null) {
            userEntity.setAge(userRegister.getAge());
        }
        return userEntity;
    }

    @Transactional
    private Collection<RoleEntity> assignRolesAccordingToUserType(UserType userType) {
        roleRepository.findByName(userType.name()).orElseGet(() -> {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User type does not exist");
        });

        Collection<RoleEntity> roles = new HashSet<>(3);

        final String userTypeName = userType.name();
        switch (userTypeName) {
            case "ADMIN":
                RoleEntity a_admin = roleRepository.findByName(UserType.ADMIN.name()).get();
                RoleEntity a_moderator = roleRepository.findByName(UserType.MODERATOR.name()).get();
                RoleEntity a_user = roleRepository.findByName(UserType.USER.name()).get();
                roles.addAll(Stream.of(a_admin, a_moderator, a_user).collect(Collectors.toSet()));
                break;

            case "MODERATOR":
                RoleEntity m_moderator = roleRepository.findByName(UserType.MODERATOR.name()).get();
                RoleEntity m_user = roleRepository.findByName(UserType.USER.name()).get();
                roles.addAll(Stream.of(m_moderator, m_user).collect(Collectors.toSet()));
                break;

            case "USER":
                RoleEntity user = roleRepository.findByName(UserType.USER.name()).get();
                roles.add(user);
                break;
        }

        return roles;
    }

}
