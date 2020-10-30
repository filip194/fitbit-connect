package hr.fitbit.demo.fitbitconnect.entity;

import hr.fitbit.demo.fitbitconnect.fixture.RoleFixture;
import hr.fitbit.demo.fitbitconnect.repository.RoleRepository;
import hr.fitbit.demo.fitbitconnect.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
public class RoleEntityTest {

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

    @Test
    @Transactional
    public void testIdAndRoleIdGenerating() {

        final RoleEntity roleEntity = RoleFixture.createRoleEntity("TEST_ROLE");

        roleRepository.save(roleEntity);

        assertThat(roleRepository.findAll().iterator().next().getId()).isNotNull();
        assertThat(roleRepository.findAll().iterator().next().getRoleId()).isNotNull();
    }
}

