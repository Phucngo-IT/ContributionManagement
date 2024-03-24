package spring.boot.contributionmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.ArticleService;
import spring.boot.contributionmanagement.services.UserService;

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

}
