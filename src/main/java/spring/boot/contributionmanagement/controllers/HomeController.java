package spring.boot.contributionmanagement.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.entities.Faculty;
import spring.boot.contributionmanagement.entities.Role;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.ArticleService;
import spring.boot.contributionmanagement.services.FileUpload;
import spring.boot.contributionmanagement.services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final ArticleService articleService;
    private final UserService userService;

    @Autowired
    public HomeController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping
    public String showHomepage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userAuth = (UserDetails) authentication.getPrincipal();


        if (userAuth != null){
            String username = userAuth.getUsername();
            User user = this.userService.findByUsername(username);
            String facultyName = user.getFaculty().getName();


            Collection<? extends GrantedAuthority> authorities = userAuth.getAuthorities();
            boolean isStudent = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_STUDENT"));
            boolean isCoordinator = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_COORDINATOR"));
            boolean isGuest = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_GUEST"));
            boolean isManager = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));
            boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            List<Article> articles = this.articleService.findAll();

            List<Article> articlesPublished = new ArrayList<>();

            if (isStudent || isCoordinator || isGuest){
                for (Article article : articles){
                    if (article.getStatus() == Article.Status.publish && article.getUser().getFaculty().getName().equals(facultyName)){
                        articlesPublished.add(article);
                    }
                }
            }
            if (isManager || isAdmin){
                for (Article article : articles){
                    if (article.getStatus() == Article.Status.publish){
                        articlesPublished.add(article);
                    }
                }
            }

            model.addAttribute("articles",articlesPublished);
            return "Home/home";
        }
        return "redirect:/login/404";
    }


    @GetMapping("/about")
    public String About(){

        return "Home/about";
    }
    @GetMapping("/help")
    public String help(){
        return "Home/help";
    }
    @GetMapping("/helpAdmin")
    public String helpAdmin(){
        return "Home/helpAdmin";
    }
    @GetMapping("/helpStudent")
    public String helpStudent(){
        return "Home/helpStudent";
    }
    @GetMapping("/helpCoor")
    public String helpCoordinator(){
        return "Home/helpCoor";
    }
    @GetMapping("/helpGuest")
    public String helpGuest(){
        return "Home/helpGuest";
    }

    @GetMapping("/helpManager")
    public String helpManager(){
        return "Home/helpManager";
    }
    @GetMapping("/term")
    public String Term(){

        return "Home/terms";
    }
    @GetMapping("/contact")
    public String contact(){

        return "Home/contact";
    }
    @GetMapping("/profile")
    public String Profile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userAuth = (UserDetails) authentication.getPrincipal();

        String username = userAuth.getUsername();
        User user = this.userService.findByUsername(username);
        model.addAttribute("user",user);
        return "User/profile";
    }
    @GetMapping("/updateProfile")
    public String UpdateProfile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userAuth = (UserDetails) authentication.getPrincipal();

        String username = userAuth.getUsername();
        User user = this.userService.findByUsername(username);
        model.addAttribute("user",user);

        return "User/updateProfile";
    }

//    @PostMapping("/saveProfile")
//    public String saveProfile(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam("image")
//    MultipartFile multipartFile, Model model, HttpSession session) throws IOException{
//
//            if (result.hasErrors()){
//                return "User/updateProfile";
//            }
//            String username = user.getUsername();
//            User userExisted = this.userService.findByUsername(username);
//            if (userExisted != null) {
////
////                BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
////                user.setPassword(bCrypt.encode(user.getPassword()));
//
//                if (!multipartFile.isEmpty()) {
//                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//                    user.setAvatar(fileName);
//
//                    this.userService.saveAndUpdate(user);
//                    String uploadDirectory = "src/main/resources/static/userAvatar/" + user.getId();
//                    FileUpload.saveFile(uploadDirectory, fileName, multipartFile);
//                } else {
//                    if (user.getAvatar().isEmpty()) {
//                        user.setAvatar(null);
//                        this.userService.saveAndUpdate(user);
//                    }
//                }
//                this.userService.save(user);
//                session.setAttribute("showUser", user);
//                return "User/profile";
//            }
//            model.addAttribute("updateProfile", "Please, login!");
//
//            return "redirect:/login";
//        }
@PostMapping("/saveProfile")
public String saveProfile(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam("image") MultipartFile multipartFile, Model model, HttpSession session) throws IOException {
    System.out.println(0);
    if (result.hasErrors()) {
        System.out.println(1);
        return "User/updateProfile";
    }
    System.out.println(2);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
        // Người dùng đã đăng nhập, tiến hành lưu profile
        System.out.println(3);
        String username = authentication.getName();
        User loggedInUser = this.userService.findByUsername(username);
        if (loggedInUser != null) {
            System.out.println(4);
            // Cập nhật thông tin profile
            loggedInUser.setFullName(user.getFullName());
            loggedInUser.setEmail(user.getEmail());
            loggedInUser.setAddress(user.getAddress());

            System.out.println(user.getFullName());
            System.out.println(user.getEmail());
            System.out.println(user.getAddress());


            if (!multipartFile.isEmpty()) {
                System.out.println(5);
                // Xử lý tải lên ảnh và cập nhật avatar
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                loggedInUser.setAvatar(fileName);
                // Lưu file vào thư mục
                String uploadDirectory = "src/main/resources/static/userAvatar/" + loggedInUser.getId();
                FileUpload.saveFile(uploadDirectory, fileName, multipartFile);
            }
            System.out.println(6);
            // Lưu thông tin người dùng
            this.userService.saveAndUpdate(loggedInUser);
//            session.setAttribute("showUser", loggedInUser);

            // Chuyển hướng đến trang profile và hiển thị thông báo thành công
            model.addAttribute("updateProfileSuccess", "Profile updated successfully!");
            return "redirect:/home/profile";
        }
    }
    System.out.println(7);

    // Người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
    model.addAttribute("updateProfile", "Please, login!");
    return "redirect:/login";
}











}



