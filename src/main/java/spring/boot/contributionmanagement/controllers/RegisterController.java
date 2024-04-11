package spring.boot.contributionmanagement.controllers;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import spring.boot.contributionmanagement.mailService.MailService;
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

    private final MailService mailService;


    @Autowired
    public RegisterController(UserService userService, FacultyService facultyService, RoleService roleService, MailService mailService) {
        this.userService = userService;
        this.facultyService = facultyService;
        this.roleService = roleService;
        this.mailService = mailService;
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


    @GetMapping("/forgot")
    public String forgotPassword (){
//        model.addAttribute("user", new User());
        return "User/forgotPassword";
    }

    @GetMapping("/send_otp")
    public String verifyEmail(@RequestParam("username") String username, Model model){
        User user = this.userService.findByUsername(username);
        if (user != null){
            String email = user.getEmail();
            this.mailService.sendOtp(email);

            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "User/verifyOTP";
        }
        model.addAttribute("notExist", "This username does not exist");
        return "User/forgotPassword";
    }

    @GetMapping("/update_password")
    public String mail(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userAuth = (UserDetails) authentication.getPrincipal();
        String username = userAuth.getUsername();
        User user = this.userService.findByUsername(username);
        if (user != null){
            String email = user.getEmail();
            this.mailService.sendOtp(email);

            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "User/verifyOTP";
        }
        model.addAttribute("notExist", "This username does not exist");
        return "User/forgotPassword";
    }


    @GetMapping("/verify")
    public String verifyOTP(@RequestParam("otp") String otp, @RequestParam("username") String username, @RequestParam("email") String email, Model model){

        if(this.mailService.verifyOtp(email, otp)){
            model.addAttribute("user", this.userService.findByUsername(username));
            return "User/changePassword";
        }
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        model.addAttribute("error", "This OTP was wrong!!!");

        return "User/verifyOTP";
    }


    @PostMapping("/save_change_password")
    public String changePass(@ModelAttribute("user") User user){
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        user.setPassword(bCrypt.encode(user.getPassword()));

        this.userService.saveAndUpdate(user);
        return "redirect:/login";
    }



}
