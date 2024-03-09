package spring.boot.contributionmanagement.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.UserService;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.boot.contributionmanagement.repositories.UserRepository;
import spring.boot.contributionmanagement.repositories.UserRepository;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserRepository userRepository;


    @Autowired
    public RegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String registerForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "User/register";// "/register/create"
    }

}
