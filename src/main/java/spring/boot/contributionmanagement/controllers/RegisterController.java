package spring.boot.contributionmanagement.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.boot.contributionmanagement.repositories.UserRepository;
import spring.boot.contributionmanagement.repositories.UserRepository;



@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserRepository userRepository;


    public RegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
