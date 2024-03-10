package spring.boot.contributionmanagement.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.boot.contributionmanagement.services.UserService;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.boot.contributionmanagement.repositories.UserRepository;
import spring.boot.contributionmanagement.repositories.UserRepository;

public class RegisterController {

    private final UserRepository userRepository;


    public RegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
