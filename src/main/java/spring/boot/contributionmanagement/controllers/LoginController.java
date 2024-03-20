package spring.boot.contributionmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/403")
    public String error403(){
        return "Error/error403";
    }

    @GetMapping("/404")
    public String error404(){
        return "Error/error404";
    }

}
