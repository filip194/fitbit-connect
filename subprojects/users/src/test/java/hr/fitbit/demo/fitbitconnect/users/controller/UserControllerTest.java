package hr.fitbit.demo.fitbitconnect.users.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import hr.fitbit.demo.fitbitconnect.testsupport.TestApplication;
import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.user.User;
import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.user.UserRegister;
import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.user.UserResponse;
import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.user.UserType;
import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.user.UserUpdate;
import hr.fitbit.demo.fitbitconnect.dao.fixture.UserFixture;
import hr.fitbit.demo.fitbitconnect.dao.repository.RoleRepository;
import hr.fitbit.demo.fitbitconnect.dao.repository.UserRepository;
import hr.fitbit.demo.fitbitconnect.testsupport.controller.ControllerTestSupport;
import hr.fitbit.demo.fitbitconnect.users.service.UserService;

@ContextConfiguration(classes = TestApplication.class)
@WebMvcTest(UserController.class)
public class UserControllerTest extends ControllerTestSupport {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = {AUTHENTICATED_USER_ROLE})
    public void shouldGetUsers() throws Exception {
        final List<User> users = createUsers();
        final Page<User> usersPage = new PageImpl<>(users, Pageable.unpaged(), users.size());

        when(userService.getUsers(anyString(), any())).thenReturn(usersPage);
        final MockHttpServletResponse response = mvc.perform(get("/api/users?username=&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeader("X-Total-Count")).isEqualTo("3");
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).isEqualTo(
                "[" + objectMapper.writeValueAsString(users.get(0)) + ","
                        + objectMapper.writeValueAsString(users.get(1)) + ","
                        + objectMapper.writeValueAsString(users.get(2)) + "]");
    }

    @Test
    @WithMockUser(roles = {AUTHENTICATED_USER_ROLE})
    public void shouldGetUsersByUsername() throws Exception {
        final List<User> users = createUsers().subList(0, 2);
        final Page<User> usersPage = new PageImpl<>(users, Pageable.unpaged(), users.size());

        when(userService.getUsers(anyString(), any())).thenReturn(usersPage);
        final MockHttpServletResponse response = mvc.perform(get("/api/users?username=user&page=0&size=10")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeader("X-Total-Count")).isEqualTo("2");
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).isEqualTo(
                "[" + objectMapper.writeValueAsString(users.get(0)) + ","
                        + objectMapper.writeValueAsString(users.get(1)) + "]");
    }

    @Test
    @WithMockUser(roles = {AUTHENTICATED_USER_ROLE})
    public void shouldGetUsersForNonExistingPage() throws Exception {
        final Page<User> usersPage = new PageImpl<>(Collections.emptyList(), Pageable.unpaged(), 0);

        when(userService.getUsers(anyString(), any())).thenReturn(usersPage);
        final MockHttpServletResponse response = mvc.perform(get("/api/users?username=&page=1&size=10")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeader("X-Total-Count")).isEqualTo("0");
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    @WithMockUser(roles = {AUTHENTICATED_USER_ROLE})
    public void shouldGetUser() throws Exception {
        final UUID userId = UUID.randomUUID();
        final User user = UserFixture.createUser(userId, "username");

        when(userService.getUser(any())).thenReturn(Optional.of(user));
        final MockHttpServletResponse response = mvc.perform(get("/api/users/" + userId)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(user));
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        final UserRegister userRegister = UserFixture.createUserRegister(
                "username",
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                UserType.USER
        );
        final UserResponse userResponse = UserFixture.createUserResponse();

        when(userService.registerUser(any())).thenReturn(userResponse);
        final MockHttpServletResponse response = mvc.perform(post("/api/users/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegister)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(userResponse));
    }

    @Test
    @WithMockUser(roles = {ADMIN_ROLE})
    public void shouldUpdateUser() throws Exception {
        final UUID userId = UUID.randomUUID();
        final UserUpdate userUpdate = UserFixture.createUserUpdate(
                "password",
                25,
                "name",
                "last_name",
                UserType.USER
        );

        when(userService.updateUser(any(), any())).thenReturn(true);
        final MockHttpServletResponse response = mvc.perform(put("/api/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdate)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    @WithMockUser(roles = {ADMIN_ROLE})
    public void shouldDeleteUser() throws Exception {
        final UUID userId = UUID.randomUUID();

        when(userService.deleteUser(any())).thenReturn(true);
        final MockHttpServletResponse response = mvc.perform(delete("/api/users/" + userId))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    private static List<User> createUsers() {
        final User user1 = UserFixture.createUser(UUID.randomUUID(), "username1");
        final User user2 = UserFixture.createUser(UUID.randomUUID(), "username2");
        final User user3 = UserFixture.createUser(UUID.randomUUID(), "admin");
        return Arrays.asList(user1, user2, user3);
    }
}

