package spring.boot.contributionmanagement.controllers;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.repositories.UserRepository;
import spring.boot.contributionmanagement.services.UserService;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String loginForm(){
        return "User/login";
    }

}
