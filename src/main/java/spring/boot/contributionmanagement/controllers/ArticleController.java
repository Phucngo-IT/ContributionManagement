package spring.boot.contributionmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import spring.boot.contributionmanagement.entities.AcademicYear;
import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.AcademicYearService;
import spring.boot.contributionmanagement.services.ArticleService;
import spring.boot.contributionmanagement.services.UserService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;



@Controller
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;
    private final AcademicYearService academicYearService;

    @Autowired
    public ArticleController(ArticleService articleService, UserService userService, AcademicYearService academicYearService) {
        this.articleService = articleService;
        this.userService = userService;
        this.academicYearService = academicYearService;
    }
//
    @GetMapping
    public String list(Model model){
            List<Article> article = articleService.findAll();
            model.addAttribute("articles", article);
            return "User/student/contributionManagement";
    }
//
        @GetMapping("/showForm")
        public String showFormArticle(Model model){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
//                Long userId = userService.findUserIdByUsername(username);
//                model.addAttribute("userId", userId);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String currentDate = (LocalDate.now().format(formatter));
                List<AcademicYear> academicYears = academicYearService.findAll();
                Article article = new Article();
                article.setUploadDate(Date.valueOf(currentDate));
                article.setUser(userService.findByUsername(username));
//                model.addAttribute("currentDate", currentDate);
                model.addAttribute("academicYears", academicYears);
                model.addAttribute("article", article);
                return "User/student/addArticle";
            } else {
                // Chưa đăng nhập
                return "redirect:/login"; // hoặc bất kỳ trang nào bạn muốn chuyển hướng đến
            }
        }
////    //
    @PostMapping("/save")
    public String addArticle(@ModelAttribute("article") Article article){
        this.articleService.saveAndUpdate(article);
        return "redirect:/student/articleList";
    }
//
    @GetMapping("/delete")
    public String deleteArticle(@RequestParam("id")Long id){
        this.articleService.deleteById(id);
        return "redirect:/student/articleList";
    }
    @GetMapping("/update")

    public String updateArticle(@RequestParam("id")Long id, Model model){
        Article article = this.articleService.findById(id);
        model.addAttribute("article", article);
        return "User/student/addArticle";
    }

}
