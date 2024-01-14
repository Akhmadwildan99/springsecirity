package wildan.learn.springsecurity.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import wildan.learn.springsecurity.domain.User;
import wildan.learn.springsecurity.repository.UserRepository;

import java.util.List;

@Controller
@RequestMapping("api/v1/")
public class UserController {

    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{login}")
    public ResponseEntity<List<User>> findUserByLogin(@PathVariable String login) {
        List<User> users = userRepository.findUserByLogin(login);
        return ResponseEntity.ok().body(users);

    }
}
