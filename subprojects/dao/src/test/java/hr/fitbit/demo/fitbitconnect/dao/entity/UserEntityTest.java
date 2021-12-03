package hr.fitbit.demo.fitbitconnect.dao.entity;

import hr.fitbit.demo.fitbitconnect.dao.fixture.UserFixture;
import hr.fitbit.demo.fitbitconnect.dao.repository.UserRepository;
import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.user.UserType;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
public class UserEntityTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @AfterEach
    @Transactional
    public void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testIdAndRoleIdGenerating() {

        final UserEntity userEntity = UserFixture.createUserEntity(
                "username",
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                UserType.MODERATOR,
                new HashSet<>()
        );

        userRepository.save(userEntity);

        assertThat(userRepository.findAll().iterator().next().getId()).isNotNull();
        assertThat(userRepository.findAll().iterator().next().getUserId()).isNotNull();
    }
}

