package hr.fitbit.demo.fitbitconnect.repository;

import hr.fitbit.demo.fitbitconnect.entity.RoleEntity;
import hr.fitbit.demo.fitbitconnect.fixture.RoleFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
public class RoleRepositoryTest {

    private final static String ROLE_1 = "ROLE_1";
    private final static String ROLE_2 = "ROLE_2";
    private final static String ROLE_3 = "ROLE_3";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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
    }

    @Test
    @Transactional
    public void shouldReturnCorrectNumberOfRoles() {
        final List<RoleEntity> roles = roleRepository.findAll();

        assertThat(roles.size()).isEqualTo(3);
        assertThrows(IndexOutOfBoundsException.class, () -> roles.get(3));
    }

    @Test
    @Transactional
    public void shouldReturnCorrectRoleNames() {
        final List<String> roleNames = roleRepository.findAll().stream()
                .map(entity -> entity.getName()).collect(Collectors.toList());

        assertThat(roleNames).contains(ROLE_1, ROLE_2, ROLE_3);
    }
}

