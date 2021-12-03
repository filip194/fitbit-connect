package hr.fitbit.demo.fitbitconnect.security;

import hr.fitbit.demo.fitbitconnect.dao.entity.RoleEntity;
import hr.fitbit.demo.fitbitconnect.dao.entity.UserEntity;
import hr.fitbit.demo.fitbitconnect.dao.repository.UserRepository;
import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.user.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<UserEntity> userEntity = userRepository.findByUsername(username);

        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        final Collection<? extends GrantedAuthority> authorities = assignAuthoritiesToUser(userEntity.get());
        final String password = userEntity.get().getPassword();
        return new UserPrincipal(username, password, authorities);
    }

    private Collection<? extends GrantedAuthority> assignAuthoritiesToUser(UserEntity userEntity) {
        final UserType userType = userEntity.getType();
        final Collection<UserAuthority> authorities = new HashSet<>();

        // by role, if edited additionally
        for (RoleEntity role : userEntity.getRoles()) {
            authorities.add(UserAuthority.valueOf(role.getName()));
        }
        authorities.add(UserAuthority.AUTHENTICATED_USER);

        return authorities;
    }
}

