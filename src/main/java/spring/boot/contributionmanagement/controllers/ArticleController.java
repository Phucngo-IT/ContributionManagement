package spring.boot.contributionmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.ArticleService;
import spring.boot.contributionmanagement.services.UserService;

import java.util.List;

@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;

    @Autowired
    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }
//
//    @GetMapping("student/articleList")
//    public String list(Model model){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication;
//        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
//            // Đã đăng nhập
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String username = userDetails.getUsername();
//            String pass = userDetails.getPassword();
//            // Lấy ID tài khoản từ UserDetails hoặc từ dịch vụ người dùng của bạn
//            Long userId = userService.getUserID(user);
////            model.addAttribute("userId", userId);
//            model.addAttribute("article", new Article());
//            return "User/student/articleList";
//        } else {
//            // Chưa đăng nhập
//            return "redirect:/login"; // hoặc bất kỳ trang nào bạn muốn chuyển hướng đến
//        }
//    }
//
//    @GetMapping("/student/showFormArticle")
//    public String showFormArticle(Model model){
//        model.addAttribute("article", new Article());
//        return "User/student/addArticle";
//    }
////    //
//    @PostMapping("/student/addArticle")
//    public String addArticle(@ModelAttribute("article") Article article){
//        this.articleService.saveAndUpdate(article);
//        return "redirect:/student/articleList";
//    }
//
//    @GetMapping("/student/deleteArticle")
//    public String deleteArticle(@RequestParam("id")Long id){
//        this.articleService.deleteById(id);
//        return "redirect:/student/articleList";
//    }
//    @GetMapping("/student/updateArticle")
//
//    public String updateArticle(@RequestParam("id")Long id, Model model){
//        Article article = this.articleService.findById(id);
//        model.addAttribute("article", article);
//        return "User/student/addArticle";
//    }

//    @GetMapping("/hello")
//    public String home(){
//        return "User/student/hello";
//    }
}
