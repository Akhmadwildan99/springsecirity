package wildan.learn.springsecurity.config;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wildan.learn.springsecurity.repository.UserRepository;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

public class DomainUserDetailService implements UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(DomainUserDetailService.class);

    private final UserRepository userRepository;

    public DomainUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) {
        log.debug("Authenticating by, {}", login);


        return userRepository.findUserByLoginIgnoreCase(login)
                .map(user -> {

                    if (!user.getIsActive().equals(true)) {
                        throw new UsernameNotFoundException("User with login: " + login + " was not activated");
                    }

                    List<GrantedAuthority> grantedAuthorities = user
                            .getUserAuthoryties()
                            .stream()
                            .map(authority -> new SimpleGrantedAuthority(authority.getName().toString()))
                            .collect(Collectors.toList());

                    return new User(user.getLogin(), user.getPasswordHash(), grantedAuthorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User: " + login + "not found"));


    }


}
