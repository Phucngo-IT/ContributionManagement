package spring.boot.contributionmanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.boot.contributionmanagement.repositories.UserRepository;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final UserRepository userRepository;


    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




}
