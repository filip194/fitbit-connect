package hr.fitbit.demo.fitbitconnect.controller;

import hr.fitbit.demo.fitbitconnect.apimodel.user.User;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserRegister;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserResponse;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserUpdate;
import hr.fitbit.demo.fitbitconnect.pagination.PaginationSetup;
import hr.fitbit.demo.fitbitconnect.security.UserRole;
import hr.fitbit.demo.fitbitconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "users")
public class UserController extends ExceptionHandlerController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured(UserRole.AUTHENTICATED_USER_ROLE)
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "List all registered users (or search by username)")
    public ResponseEntity<List<User>> getUsers(@RequestParam(name = "username", required = false) String username, HttpServletRequest request,
                                               @ParameterObject @PageableDefault(page = 0, size = 100, sort = "username", direction = Sort.Direction.ASC) Pageable pageRequest) {
        log.info("List all users");
        log.info("...search by username={}", username == null ? "" : username);

        final Page<User> usersPage = userService.getUsers(username, pageRequest);
        return PaginationSetup.returnContentAndHeadersWithPagination(usersPage, request);
    }

    @Secured(UserRole.AUTHENTICATED_USER_ROLE)
    @GetMapping("/{user_id}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Get user")
    public ResponseEntity<User> getUser(@PathVariable("user_id") @Parameter(name = "user_id", required = true) UUID userId) {
        log.info("Get user={}", userId);

        final Optional<User> user = userService.getUser(userId);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // registration endpoint - unsecured
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Register user")
    public ResponseEntity<UserResponse> registerUser(@Validated @RequestBody UserRegister userRegister) {
        final UserResponse userResponse = userService.registerUser(userRegister);
        log.info("Register user={}", userResponse.getUserId());
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @Secured(UserRole.ADMIN_ROLE)
    @PutMapping(value = "/{user_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Update user")
    public ResponseEntity<Void> updateUser(@PathVariable("user_id") @Parameter(name = "user_id", required = true) UUID userId,
                                           @Validated @RequestBody UserUpdate userUpdate) {
        log.info("Update user={}", userId);

        boolean updated = userService.updateUser(userId, userUpdate);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @Secured(UserRole.ADMIN_ROLE)
    @DeleteMapping(value = "/{user_id}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable("user_id") @Parameter(name = "user_id", required = true) UUID userId) {
        log.info("Delete user={}", userId);

        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

