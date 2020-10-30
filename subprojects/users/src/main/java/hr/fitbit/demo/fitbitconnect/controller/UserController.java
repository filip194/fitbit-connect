package hr.fitbit.demo.fitbitconnect.controller;

import hr.fitbit.demo.fitbitconnect.apimodel.user.User;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserRegister;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserResponse;
import hr.fitbit.demo.fitbitconnect.apimodel.user.UserUpdate;
import hr.fitbit.demo.fitbitconnect.pagination.PaginationSetup;
import hr.fitbit.demo.fitbitconnect.security.UserRole;
import hr.fitbit.demo.fitbitconnect.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "users")
public class UserController extends ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured(UserRole.AUTHENTICATED_USER_ROLE)
    @GetMapping
    @ApiOperation(value = "List all registered users (or search by username)")
    public ResponseEntity<List<User>> getUsers(@RequestParam(name = "username", required = false) String username, HttpServletRequest request, Pageable pageRequest) {
        LOG.info("List all users");
        LOG.info("...search by username={}", username == null ? "" : username);

        final Page<User> usersPage = userService.getUsers(username, pageRequest);
        return PaginationSetup.returnContentAndHeadersWithPagination(usersPage, request);
    }

    @Secured(UserRole.AUTHENTICATED_USER_ROLE)
    @GetMapping("/{user_id}")
    @ApiOperation(value = "Get user")
    public ResponseEntity<User> getUser(@PathVariable("user_id") @ApiParam(value = "user id", required = true) UUID userId) {
        LOG.info("Get user={}", userId);

        Optional<User> user = userService.getUser(userId);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    // registration endpoint - unsecured
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Register user")
    public ResponseEntity<UserResponse> registerUser(@Validated @RequestBody @ApiParam(name = "user register information", value = "user register fields", required = true) UserRegister userRegister) {
        UserResponse userResponse = userService.registerUser(userRegister);
        LOG.info("Register user={}", userResponse.getUserId());
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @Secured(UserRole.ADMIN_ROLE)
    @PutMapping(value = "/{user_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update user")
    public ResponseEntity<Void> updateUser(@PathVariable("user_id") @ApiParam(value = "user id", required = true) UUID userId,
                                           @Validated @RequestBody @ApiParam(name = "user update information", value = "user update fields", required = true) UserUpdate userUpdate) {
        LOG.info("Update user={}", userId);

        boolean updated = userService.updateUser(userId, userUpdate);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @Secured(UserRole.ADMIN_ROLE)
    @DeleteMapping(value = "/{user_id}")
    @ApiOperation(value = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable("user_id") @ApiParam(value = "user id", required = true) UUID userId) {
        LOG.info("Delete user={}", userId);

        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

