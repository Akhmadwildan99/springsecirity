package wildan.learn.springsecurity.web.rest;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wildan.learn.springsecurity.dao.AuthenticationReq;
import wildan.learn.springsecurity.dao.AuthenticationRes;
import wildan.learn.springsecurity.dao.ReturnMessage;
import wildan.learn.springsecurity.dao.UserDao;
import wildan.learn.springsecurity.domain.SecurityUser;
import wildan.learn.springsecurity.domain.User;
import wildan.learn.springsecurity.domain.UserAuthoryty;
import wildan.learn.springsecurity.domain.enumeration.UserAuthorityType;
import wildan.learn.springsecurity.repository.UserRepository;
import wildan.learn.springsecurity.service.JwtService;
import wildan.learn.springsecurity.service.UserService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/api/v1/auth")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserRepository userRepository, UserService userServic, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userService = userServic;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/user/{login}")
    public ResponseEntity<List<User>> findUserByLogin(@PathVariable String login) {
        List<User> users = userRepository.findUserByLogin(login);
        return ResponseEntity.ok().body(users);

    }

    @PostMapping("/register")
    public ResponseEntity<ReturnMessage> registerUser(@RequestBody UserDao userDao) throws URISyntaxException {
        log.debug("REGISTER USER: {}", userDao.getEmail());
        ReturnMessage ret = new ReturnMessage();
        ret.setId(-1);

        // validate login unique
        if(!userRepository.findUserByLogin(userDao.getEmail()).isEmpty()) {
            ret.setMessage("Email "+ userDao.getEmail() + " is Exist");
            return  ResponseEntity.badRequest().body(ret);
        }

        // validate if get user auth admin

        if(userDao.getUserAuthoryties() != null ) {
            ret.setMessage("Authority must not set at register");
            return  ResponseEntity.badRequest().body(ret);
        }

        UserAuthoryty userAuthoryty = new UserAuthoryty();
        userAuthoryty.setName(UserAuthorityType.USER);
        Set<UserAuthoryty>  userAuthorytySet  = Set.of(userAuthoryty);
        userDao.setUserAuthoryties(userAuthorytySet);

        ret = this.userService.registerUser(userDao);

        return ResponseEntity.created(new URI("/api/v1/register")).body(ret);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationRes> authenticate(
            @Valid @RequestBody AuthenticationReq request
    ) {
        log.debug("LOGIN REQUSET, {}", request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findUserByLoginIgnoreCase(request.getEmail()).orElseThrow();
        UserDetails userDetails = new SecurityUser(user);
        String jwtToken =  jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        AuthenticationRes res = new AuthenticationRes();
        res.setAccessToken(jwtToken);
        res.setRefreshToken(refreshToken);
        return ResponseEntity.ok().body(res);
    }

}
