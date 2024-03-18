package spring.boot.contributionmanagement.controllers;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import spring.boot.contributionmanagement.entities.AcademicYear;
import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.mailService.MailService;
import spring.boot.contributionmanagement.mailService.MailStructure;
import spring.boot.contributionmanagement.services.AcademicYearService;
import spring.boot.contributionmanagement.services.ArticleService;
import spring.boot.contributionmanagement.services.FileUpload;
import spring.boot.contributionmanagement.services.UserService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;



@Controller
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;
    private final AcademicYearService academicYearService;
    private final MailService mailService;
    private final MailStructure mailStructure;
    public static final String DIRECTORY = System.getProperty("user.home") + "/OneDrive - Phucngocomputer/Desktop/ContributionManagement/src/main/resources/static/wordFiles/";


    @Autowired
    public ArticleController(ArticleService articleService, UserService userService, AcademicYearService academicYearService, MailService mailService, MailStructure mailStructure) {
        this.articleService = articleService;
        this.userService = userService;
        this.academicYearService = academicYearService;
        this.mailService = mailService;
        this.mailStructure = mailStructure;
    }

    @GetMapping()
    public String list(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();


        if (user != null) {
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            boolean isStudent = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_STUDENT"));

            boolean isCoordinator = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_COORDINATOR"));



            if (isStudent) {
                String username = user.getUsername();
                User userObj = this.userService.findByUsername(username);

                List<Article> articles = userObj.getArticles();

                model.addAttribute("articles", articles);
                return "User/student/contributionManagement";

            } else if (isCoordinator) {

                List<Article> articles = this.articleService.findAll();
                model.addAttribute("articles", articles);
                return "User/coordinator/feedbackManagement";

            } else {
                model.addAttribute("articles", new Article());
                return "User/student/contributionManagement";
            }
        }else {
            return null;
        }

    }

    @GetMapping("/showdetail")
        public String showdetail(@PathParam("id") Long id, Model model){
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "User/manager/ViewdetailContribution";
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
    public String addArticle(@ModelAttribute("article") Article article, @RequestParam("files")MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        article.setFileName(fileName);
        this.articleService.save(article);


        String uploadDirectory = DIRECTORY;
        FileUpload.saveFile(uploadDirectory, fileName, multipartFile);

        this.articleService.save(article);

        this.articleService.saveAndUpdate(article);

        this.mailService.sendMail("phucnhgcc210017@fpt.edu.vn", mailStructure);
        return "redirect:/article";
    }
//
    @GetMapping("/delete")
    public String deleteArticle(@RequestParam("id")Long id){
        this.articleService.deleteById(id);
        return "redirect:/article";
    }
    @GetMapping("/update")
    public String updateArticle(@RequestParam("id")Long id, Model model){
        Article article = this.articleService.findById(id);
//        List<AcademicYear> academicYears = academicYearService.findAll();
        model.addAttribute("article", article);
        return "User/student/addArticle";
    }


    @GetMapping("/feedback_management")
    public String showFeedbackManagement(Model model){
       List<Article> article = this.articleService.findAll();
        model.addAttribute("articles",article);
        return "User/coordinator/feedbackManagement";
    }






}
