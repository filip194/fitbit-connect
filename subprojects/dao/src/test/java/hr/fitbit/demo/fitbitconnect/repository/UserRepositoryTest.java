package hr.fitbit.demo.fitbitconnect.repository;

import hr.fitbit.demo.fitbitconnect.entity.RoleEntity;
import hr.fitbit.demo.fitbitconnect.entity.UserEntity;
import hr.fitbit.demo.fitbitconnect.fixture.RoleFixture;
import hr.fitbit.demo.fitbitconnect.fixture.UserFixture;
import hr.fitbit.demo.fitbitconnect.util.UserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
public class UserRepositoryTest {

    private final static String ROLE_1 = "ROLE_1";
    private final static String ROLE_2 = "ROLE_2";
    private final static String ROLE_3 = "ROLE_3";

    private final static String USERNAME_1 = "USERNAME_1";
    private final static String USERNAME_2 = "USERNAME_2";
    private final static String USERNAME_3 = "USERNAME_3";
    private static final String USERNAME_4 = "USERNAME_4";

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @AfterEach
    @Transactional
    public void cleanDatabase() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @BeforeEach
    @Transactional
    public void setup() {
        final RoleEntity roleEntity1 = RoleFixture.createRoleEntity(ROLE_1);
        final RoleEntity roleEntity2 = RoleFixture.createRoleEntity(ROLE_2);
        final RoleEntity roleEntity3 = RoleFixture.createRoleEntity(ROLE_3);

        final Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleEntity1);
        roles.add(roleEntity2);
        roles.add(roleEntity3);

        roleRepository.saveAll(roles);

        userRepository.saveAll(createUserEntities());
    }

    @Test
    @Transactional
    public void shouldReturnCorrectNumberOfUsers() {
        final List<UserEntity> users = userRepository.findAll();

        assertThat(users.size()).isEqualTo(4);
        assertThrows(IndexOutOfBoundsException.class, () -> users.get(4));
    }

    @Test
    @Transactional
    public void shouldReturnCorrectUsernames() {
        final List<String> usernames = userRepository.findAll().stream()
                .map(entity -> entity.getUsername()).collect(Collectors.toList());

        assertThat(usernames).contains(USERNAME_1, USERNAME_2, USERNAME_3, USERNAME_4);
    }

    @Test
    @Transactional
    public void shouldAssignRolesToUsers() {
        roleRepository.deleteAll();
        userRepository.deleteAll();

        final RoleEntity roleEntity1 = RoleFixture.createRoleEntity(ROLE_1);
        final RoleEntity roleEntity2 = RoleFixture.createRoleEntity(ROLE_2);
        final RoleEntity roleEntity3 = RoleFixture.createRoleEntity(ROLE_3);

        final Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleEntity1);
        roles.add(roleEntity2);
        roles.add(roleEntity3);

        roleRepository.saveAll(roles);

        final List<UserEntity> users = createUserEntities();

        userRepository.saveAll(users);

        final List<UUID> userIds = userRepository.findAll().stream()
                .map(userEntity -> userEntity.getUserId()).collect(Collectors.toList());

        users.get(0).addRole(roleEntity1);
        users.get(1).addRole(roleEntity2);
        users.get(2).addRole(roleEntity3);
        users.get(3).addAllRoles(roles);

        userRepository.saveAndFlush(users.get(0));
        userRepository.saveAndFlush(users.get(1));
        userRepository.saveAndFlush(users.get(2));
        userRepository.saveAndFlush(users.get(3));

        assertThat(userRepository.findByUserId(users.get(0).getUserId()).get().getRoles().iterator().next().getName()).isEqualTo(ROLE_1);
        assertThat(userRepository.findByUserId(users.get(1).getUserId()).get().getRoles().iterator().next().getName()).isEqualTo(ROLE_2);
        assertThat(userRepository.findByUserId(users.get(2).getUserId()).get().getRoles().iterator().next().getName()).isEqualTo(ROLE_3);
        assertThat(userRepository.findByUserId(users.get(3).getUserId()).get().getRoles().stream()
                .map(roleEntity -> roleEntity.getName()).collect(Collectors.toList())).contains(ROLE_1, ROLE_2, ROLE_3);
    }

    private List<UserEntity> createUserEntities() {
        final UserEntity userEntity1 = UserFixture.createUserEntity(
                USERNAME_1,
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                UserType.USER,
                new HashSet<>()
        );
        final UserEntity userEntity2 = UserFixture.createUserEntity(
                USERNAME_2,
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                UserType.USER,
                new HashSet<>()
        );
        final UserEntity userEntity3 = UserFixture.createUserEntity(
                USERNAME_3,
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                UserType.USER,
                new HashSet<>()
        );
        final UserEntity userEntity4 = UserFixture.createUserEntity(
                USERNAME_4,
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                UserType.USER,
                new HashSet<>()
        );

        final List<UserEntity> users = new ArrayList<>(4);
        users.add(userEntity1);
        users.add(userEntity2);
        users.add(userEntity3);
        users.add(userEntity4);

        return users;
    }
}

