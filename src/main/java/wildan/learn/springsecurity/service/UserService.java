package wildan.learn.springsecurity.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import wildan.learn.springsecurity.domain.User;
import wildan.learn.springsecurity.repository.UserRepository;
import wildan.learn.springsecurity.service.dto.UserDto;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(UserDto userDto) {
        // log info

        // encode password

        // mapping user authorities

        // save user

        return new User();
    }
}
