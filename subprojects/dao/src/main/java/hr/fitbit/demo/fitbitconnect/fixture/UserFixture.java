package hr.fitbit.demo.fitbitconnect.fixture;

import hr.fitbit.demo.fitbitconnect.apimodel.user.User;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserRegister;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserResponse;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserUpdate;
import hr.fitbit.demo.fitbitconnect.entity.RoleEntity;
import hr.fitbit.demo.fitbitconnect.entity.UserEntity;
import hr.fitbit.demo.fitbitconnect.util.UserType;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

public class UserFixture {

    public static User createUser(UUID userId, String username) {
        final User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setEmail("email@user.net");
        user.setAge(30);
        user.setName("first_name");
        user.setLastName("last_name");
        user.setType(UserType.USER);
        return user;
    }

    public static UserRegister createUserRegister(String username, String password, String email, int age,
                                                  String name, String lastName, UserType type) {
        final UserRegister userRegister = new UserRegister();
        userRegister.setUsername(username);
        userRegister.setPassword(password);
        userRegister.setEmail(email);
        userRegister.setAge(age);
        userRegister.setName(name);
        userRegister.setLastName(lastName);
        userRegister.setType(type);
        return userRegister;
    }

    public static UserUpdate createUserUpdate(String password, int age, String name, String lastName, UserType type) {
        final UserUpdate userUpdate = new UserUpdate();
        userUpdate.setName(name);
        userUpdate.setLastName(lastName);
        userUpdate.setAge(age);
        userUpdate.setPassword(password);
        userUpdate.setType(type);
        return userUpdate;
    }

    public static UserResponse createUserResponse() {
        final UserResponse userResponse = new UserResponse();
        userResponse.setUsername("username");
        userResponse.setType(UserType.USER);
        userResponse.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000101"));
        return userResponse;
    }

    public static UserEntity createUserEntity(String username, String password, String email, int age, String name,
                                              String lastName, UserType type, Collection<RoleEntity> roles) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setEmail(email);
        userEntity.setAge(age);
        userEntity.setName(name);
        userEntity.setLastName(lastName);
        userEntity.setType(type);
        userEntity.setRoles(roles);
        userEntity.setCreated(new Timestamp(System.currentTimeMillis()));
        userEntity.setUpdated(new Timestamp(System.currentTimeMillis()));
        return userEntity;
    }
}

