package spring.boot.contributionmanagement.controllers;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.boot.contributionmanagement.entities.Faculty;
import spring.boot.contributionmanagement.entities.Role;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.FacultyService;
import spring.boot.contributionmanagement.services.FileUpload;
import spring.boot.contributionmanagement.services.RoleService;
import spring.boot.contributionmanagement.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;
    private final FacultyService facultyService;
    private final RoleService roleService;


    @Autowired
    public RegisterController(UserService userService, FacultyService facultyService, RoleService roleService) {
        this.userService = userService;
        this.facultyService = facultyService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String registerForm(Model model){

        User user = new User();
        List<Faculty> faculties = this.facultyService.findAll();
        Role role = this.roleService.findById(5L);

        model.addAttribute("role", role);
        model.addAttribute("faculties", faculties);
        model.addAttribute("user", user);

        return "User/register";// "/register/create"
    }

    @GetMapping("/add_new_account")
    public String addNewAcc(Model model){
        User newAccount = new User();
        List<Faculty> faculties = this.facultyService.findAll();
        List<Role> roles = this.roleService.findAll();

        model.addAttribute("roles", roles);
        model.addAttribute("faculties", faculties);
        model.addAttribute("user", newAccount);

        return "User/register";
    }


    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result,@RequestParam("image")
    MultipartFile multipartFile, Model model, HttpSession session) throws IOException {

        if (result.hasErrors()){
            return "User/register";//if has any error, return register form
        }
        String username = user.getUsername();
        User userExisted = this.userService.findByUsername(username);
        if (userExisted == null) {

            BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
            user.setPassword(bCrypt.encode(user.getPassword()));

            if (!multipartFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                user.setAvatar(fileName);

                this.userService.saveAndUpdate(user);
                String uploadDirectory = "src/main/resources/static/userAvatar/" + user.getId();
                FileUpload.saveFile(uploadDirectory, fileName, multipartFile);
            } else {
                if (user.getAvatar().isEmpty()) {
                    user.setAvatar(null);
                    this.userService.saveAndUpdate(user);
                }
            }
            this.userService.save(user);
            session.setAttribute("showUser", user);
            return "redirect:/login";
        }

        //if the user already existed, system will return register page
        List<Faculty> faculties = this.facultyService.findAll();
        List<Role> roles = this.roleService.findAll();

        model.addAttribute("roles", roles);
        model.addAttribute("faculties", faculties);
        model.addAttribute("user", new User());

        //Send a message to register page
        model.addAttribute("userExisted", "This user existed, please entering another information");

        return "User/register";// "/register/create"
    }


    @GetMapping("/account_management")
    public String showAccount(Model model){
        model.addAttribute("accounts", this.userService.findAll());

        return "User/admin/accountManagement";
    }




}
