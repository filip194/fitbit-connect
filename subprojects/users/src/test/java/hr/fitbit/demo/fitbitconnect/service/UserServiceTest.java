package hr.fitbit.demo.fitbitconnect.service;

import hr.fitbit.demo.fitbitconnect.apimodel.user.User;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserRegister;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserResponse;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserUpdate;
import hr.fitbit.demo.fitbitconnect.entity.RoleEntity;
import hr.fitbit.demo.fitbitconnect.entity.UserEntity;
import hr.fitbit.demo.fitbitconnect.fixture.RoleFixture;
import hr.fitbit.demo.fitbitconnect.fixture.UserFixture;
import hr.fitbit.demo.fitbitconnect.repository.RoleRepository;
import hr.fitbit.demo.fitbitconnect.repository.UserRepository;
import hr.fitbit.demo.fitbitconnect.util.UserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    private static final String USERNAME_COLUMN = "username";

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    @AfterEach
    public void cleanDatabase() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @Transactional
    public void shouldRegisterUser() {
        roleRepository.saveAndFlush(RoleFixture.createRoleEntity(UserType.USER.name()));

        final String username = "username";
        final UserType type = UserType.USER;

        final UserRegister userRegister = UserFixture.createUserRegister(
                username,
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                type
        );
        final UserResponse userResponse = userService.registerUser(userRegister);

        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getUserId()).isNotNull();
        assertThat(userResponse.getUsername()).isEqualTo(username);
        assertThat(userResponse.getType()).isEqualTo(type);

        assertThat(userRepository.findByUserId(userResponse.getUserId())).isNotEmpty();
    }

    @Test
    @Transactional
    public void shouldUpdateUser() {
        createUsersAndRoles();

        final UUID userId = userRepository.findAll().get(0).getUserId();

        final UserUpdate userUpdate = UserFixture.createUserUpdate(
                "password_update",
                30,
                "name_update",
                "last_name_update",
                UserType.MODERATOR
        );
        final boolean updated = userService.updateUser(userId, userUpdate);
        final UserEntity updatedUser = userRepository.findByUserId(userId).get();

        assertThat(updated).isTrue();
        assertThat(updatedUser.getPassword()).contains("$2y$12$"); //is encrypted
        assertThat(updatedUser.getAge()).isEqualTo(30);
        assertThat(updatedUser.getName()).isEqualTo("name_update");
        assertThat(updatedUser.getLastName()).isEqualTo("last_name_update");
        assertThat(updatedUser.getType()).isEqualTo(UserType.MODERATOR);
        assertThat(updatedUser.getRoles().stream().map(roleEntity -> roleEntity.getName()).collect(Collectors.toList()))
                .containsAnyOf(UserType.USER.name());
    }

    @Test
    @Transactional
    public void shouldDeleteUser() {
        createUsersAndRoles();

        final UUID userId = userRepository.findAll().get(0).getUserId();
        final boolean deleted = userService.deleteUser(userId);

        assertThat(deleted).isTrue();
        assertThat(userRepository.findByUserId(userId)).isEmpty();
    }

    @Test
    @Transactional
    public void shouldGetUser() {
        createUsersAndRoles();

        final UUID userId = userRepository.findAll().get(0).getUserId();
        final Optional<User> user = userService.getUser(userId);

        assertThat(user).isNotEmpty();
        assertThat(user.get().getUsername()).isEqualTo("username");
        assertThat(user.get().getAge()).isEqualTo(25);
        assertThat(user.get().getEmail()).isEqualTo("email@test.com");
        assertThat(user.get().getName()).isEqualTo("name");
        assertThat(user.get().getLastName()).isEqualTo("last_name");
        assertThat(user.get().getType()).isEqualTo(UserType.USER);
    }

    @Test
    @Transactional
    public void shouldGetUsers() {
        createUsersAndRoles();

        final Page<User> usersPage = userService.getUsers(null, PageRequest.of(0, 10));

        assertThat(usersPage.getTotalElements()).isEqualTo(4);
        assertThat(usersPage.getTotalPages()).isEqualTo(1);
        assertThat(usersPage.getContent().size()).isEqualTo(4);
        assertThat(usersPage.getContent().stream().map(_user -> _user.getUsername()).collect(Collectors.toList()))
                .contains("admin", "moderator", "username", "username2");
    }

    @Test
    @Transactional
    public void shouldGetTwoUsersPerPage() {
        createUsersAndRoles();

        final Page<User> usersPage = userService.getUsers(null, PageRequest.of(0, 2));

        assertThat(usersPage.getTotalElements()).isEqualTo(4);
        assertThat(usersPage.getTotalPages()).isEqualTo(2);
        assertThat(usersPage.getContent().size()).isEqualTo(2);
        assertThat(usersPage.getContent().stream().map(_user -> _user.getUsername()).collect(Collectors.toList()))
                .contains("username", "username2");

        final Page<User> usersPage2 = userService.getUsers(null, PageRequest.of(1, 2));

        assertThat(usersPage2.getTotalElements()).isEqualTo(4);
        assertThat(usersPage2.getTotalPages()).isEqualTo(2);
        assertThat(usersPage2.getContent().size()).isEqualTo(2);
        assertThat(usersPage2.getContent().stream().map(_user -> _user.getUsername()).collect(Collectors.toList()))
                .contains("admin", "moderator");
    }

    @Test
    @Transactional
    public void shouldSearchUsersByUsername() {
        createUsersAndRoles();

        final Page<User> usersPage = userService.getUsers("user", PageRequest.of(0, 10));

        assertThat(usersPage.getTotalElements()).isEqualTo(2);
        assertThat(usersPage.getTotalPages()).isEqualTo(1);
        assertThat(usersPage.getContent().size()).isEqualTo(2);
        assertThat(usersPage.getContent().stream().map(_user -> _user.getUsername()).collect(Collectors.toList()))
                .containsExactly("username", "username2");
    }

    @Test
    @Transactional
    public void shouldGetUsersAndSortAsc() {
        createUsersAndRolesForSorting();

        final Page<User> usersPage = userService.getUsers(null, PageRequest.of(0, 10, Sort.Direction.ASC, USERNAME_COLUMN));

        assertThat(usersPage.getTotalElements()).isEqualTo(4);
        assertThat(usersPage.getTotalPages()).isEqualTo(1);
        assertThat(usersPage.getContent().size()).isEqualTo(4);
        assertThat(usersPage.getContent().stream().map(_user -> _user.getUsername()).collect(Collectors.toList()))
                .containsExactly("username", "username2", "username3", "usernameA");
    }

    @Test
    @Transactional
    public void shouldGetUsersAndSortDesc() {
        createUsersAndRolesForSorting();

        final Page<User> usersPage = userService.getUsers(null, PageRequest.of(0, 10, Sort.Direction.DESC, USERNAME_COLUMN));

        assertThat(usersPage.getTotalElements()).isEqualTo(4);
        assertThat(usersPage.getTotalPages()).isEqualTo(1);
        assertThat(usersPage.getContent().size()).isEqualTo(4);
        assertThat(usersPage.getContent().stream().map(_user -> _user.getUsername()).collect(Collectors.toList()))
                .containsExactly("usernameA", "username3", "username2", "username");
    }

    @Test
    @Transactional
    public void shouldGetUsersAndSearchByUsernameAndSortAsc() {
        createUsersAndRoles();

        final Page<User> usersPage = userService.getUsers("user", PageRequest.of(0, 2, Sort.Direction.ASC, USERNAME_COLUMN));

        assertThat(usersPage.getTotalElements()).isEqualTo(2);
        assertThat(usersPage.getTotalPages()).isEqualTo(1);
        assertThat(usersPage.getContent().size()).isEqualTo(2);
        assertThat(usersPage.getContent().stream().map(_user -> _user.getUsername()).collect(Collectors.toList()))
                .containsExactly("username", "username2");
    }

    @Transactional
    private void createUsersAndRoles() {
        final RoleEntity roleUser = RoleFixture.createRoleEntity(UserType.USER.name());
        final RoleEntity roleModerator = RoleFixture.createRoleEntity(UserType.MODERATOR.name());
        final RoleEntity roleAdmin = RoleFixture.createRoleEntity(UserType.ADMIN.name());
        roleRepository.save(roleUser);
        roleRepository.save(roleModerator);
        roleRepository.save(roleAdmin);

        final UserEntity user = UserFixture.createUserEntity(
                "username",
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                UserType.USER,
                Collections.singleton(roleUser)
        );
        final UserEntity user2 = UserFixture.createUserEntity(
                "username2",
                "password2",
                "email@test2.com",
                25,
                "name2",
                "last_name2",
                UserType.USER,
                Collections.singleton(roleUser)
        );
        final UserEntity moderator = UserFixture.createUserEntity(
                "moderator",
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                UserType.MODERATOR,
                Collections.singleton(roleModerator)
        );
        final UserEntity admin = UserFixture.createUserEntity(
                "admin",
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                UserType.ADMIN,
                Collections.singleton(roleAdmin)
        );

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(admin);
        userRepository.save(moderator);

        roleAdmin.addUser(admin);
        roleModerator.addUser(moderator);
        roleUser.addAllUsers(Arrays.asList(user, user2));

        roleRepository.save(roleAdmin);
        roleRepository.save(roleModerator);
        roleRepository.save(roleUser);
    }


    @Transactional
    private void createUsersAndRolesForSorting() {
        final RoleEntity roleUser = RoleFixture.createRoleEntity(UserType.USER.name());
        roleRepository.save(roleUser);

        final UserEntity user2 = UserFixture.createUserEntity(
                "username2",
                "password2",
                "email@test2.com",
                25,
                "name2",
                "last_name2",
                UserType.USER,
                Collections.singleton(roleUser)
        );
        final UserEntity user = UserFixture.createUserEntity(
                "username",
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                UserType.USER,
                Collections.singleton(roleUser)
        );
        final UserEntity user3 = UserFixture.createUserEntity(
                "username3",
                "password3",
                "email@test3.com",
                25,
                "name3",
                "last_name3",
                UserType.USER,
                Collections.singleton(roleUser)
        );
        final UserEntity userA = UserFixture.createUserEntity(
                "usernameA",
                "passwordA",
                "email@testA.com",
                25,
                "nameA",
                "last_nameA",
                UserType.USER,
                Collections.singleton(roleUser)
        );

        userRepository.saveAll(Arrays.asList(user2, userA, user3, user));

        roleUser.addAllUsers(Arrays.asList(user2, userA, user3, user));

        roleRepository.save(roleUser);
    }
}

