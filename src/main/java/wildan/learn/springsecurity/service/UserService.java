package wildan.learn.springsecurity.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wildan.learn.springsecurity.dao.ReturnMessage;
import wildan.learn.springsecurity.domain.User;
import wildan.learn.springsecurity.repository.UserAuthorityRepository;
import wildan.learn.springsecurity.repository.UserRepository;
import wildan.learn.springsecurity.dao.UserDao;

import java.time.Instant;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserAuthorityRepository userAuthorityRepository;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserAuthorityRepository userAuthorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAuthorityRepository = userAuthorityRepository;
    }

    public ReturnMessage registerUser(UserDao userDto) {

        // log info
        log.info("SERVICE REGISTER USER");

        ReturnMessage ret = new ReturnMessage();
        ret.setId(-1);

        User user = new User();
        // encode password
        String encodePass = passwordEncoder.encode(userDto.getPassword());

        // mapping user and authorities
        user.setEmail(userDto.getEmail());
        user.setLogin(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPasswordHash(encodePass);
        user.setCreatedDate(Instant.now());
        user.setCreatedBy(userDto.getEmail());
        user.setIsActive(true);
//        user.setUserAuthoryties(userDto.getUserAuthoryties());

        // save user
        user = userRepository.save(user);
        if (user.getId() != null) {
            User finalUser = user;
            userDto.getUserAuthoryties()
                    .forEach(userAuthoryty -> {
                        userAuthoryty.setUser(finalUser);

                        userAuthorityRepository.save(userAuthoryty);
                    });

            ret.setId(1);
        }
        return ret;
    }
}
