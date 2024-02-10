package wildan.learn.springsecurity.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/hello")
@RestController
public class HelloController {

    @GetMapping
    public ResponseEntity<String> helloGet() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("details: "+ details.toString());
        System.out.println("principal: "+ principal.toString());
        return ResponseEntity.ok().body("HELLO") ;
    }
}
