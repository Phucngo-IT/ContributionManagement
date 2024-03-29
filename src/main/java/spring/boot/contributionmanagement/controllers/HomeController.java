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
import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.entities.Faculty;
import spring.boot.contributionmanagement.entities.Role;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.ArticleService;
import spring.boot.contributionmanagement.services.FileUpload;
import spring.boot.contributionmanagement.services.UserService;

import java.io.IOException;
import java.util.ArrayList;
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

        String username = userAuth.getUsername();
        User user = this.userService.findByUsername(username);
        String facultyName = user.getFaculty().getName();

//        Collection<? extends GrantedAuthority> authorities = userAuth.getAuthorities();
//
//        boolean isManager = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));
//

        List<Article> articles = this.articleService.findAll();

        List<Article> approvedArticles = new ArrayList<>();

        List<Article> articlesWithFacultyAuths = new ArrayList<>();

//        List<String> fileNames = new ArrayList<>();//


//        String facultyName = null;

//        User coordinatorUser = null;

        for (Article article : articles) {
            if (article.isStatus()) {
                approvedArticles.add(article);

//                fileNames.add(article.getFileName());
//
//                facultyName = article.getUser().getFaculty().getName();
//                Long userId = this.userService.findUserByFacultyAndRole(facultyName);
//                coordinatorUser = this.userService.findById(userId);
            }
        }

        for (Article article : approvedArticles){
            if (article.getUser().getFaculty().getName().equals(facultyName)) {
                articlesWithFacultyAuths.add(article);
            }

        }


//        String fileNamesString = String.join(",", fileNames);//make ArrayList file name into a single string

        model.addAttribute("articles",articlesWithFacultyAuths);
//        model.addAttribute("coordinatorUser", coordinatorUser);
//        model.addAttribute("fileNames", fileNamesString);

//        return "User/manager/approvalArticleManagement";
        return "Home/home";
    }

    @GetMapping("/about")
    public String About(){

        return "Home/about";
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
    if (result.hasErrors()) {
        return "User/updateProfile";
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
        // Người dùng đã đăng nhập, tiến hành lưu profile
        String username = authentication.getName();
        User loggedInUser = this.userService.findByUsername(username);
        if (loggedInUser != null) {
            // Cập nhật thông tin profile
            loggedInUser.setFullName(user.getFullName());
            loggedInUser.setEmail(user.getEmail());
            loggedInUser.setAddress(user.getAddress());

            if (!multipartFile.isEmpty()) {
                // Xử lý tải lên ảnh và cập nhật avatar
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                loggedInUser.setAvatar(fileName);
                // Lưu file vào thư mục
                String uploadDirectory = "src/main/resources/static/userAvatar/" + loggedInUser.getId();
                FileUpload.saveFile(uploadDirectory, fileName, multipartFile);
            }

            // Lưu thông tin người dùng
            this.userService.saveAndUpdate(loggedInUser);
//            session.setAttribute("showUser", loggedInUser);

            // Chuyển hướng đến trang profile và hiển thị thông báo thành công
            model.addAttribute("updateProfileSuccess", "Profile updated successfully!");
            return "redirect:/home/profile";
        }
    }

    // Người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
    model.addAttribute("updateProfile", "Please, login!");
    return "redirect:/login";
}











}



