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
import spring.boot.contributionmanagement.entities.Comment;
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
import java.util.ArrayList;
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

    //show role Student, Admin, Manager and Coordinator
    @GetMapping()
    public String list(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userAuth = (UserDetails) authentication.getPrincipal();

        if (userAuth != null) {
            Collection<? extends GrantedAuthority> authorities = userAuth.getAuthorities();

            boolean isStudent = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_STUDENT"));
            boolean isCoordinator = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_COORDINATOR"));
            boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            boolean isManager = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

            if (isStudent) {
                String username = userAuth.getUsername();
                User user = this.userService.findByUsername(username);

                List<Article> articles = user.getArticles();

                model.addAttribute("articles", articles);
                return "User/student/contributionManagement";

            } else if (isCoordinator) {

                List<Article> articles = this.articleService.findAll();
                model.addAttribute("articles", articles);
                return "User/coordinator/feedbackManagement";

            } else if (isAdmin) {
                List<Article> articles = this.articleService.findAll();
                model.addAttribute("articles", articles);
                return "User/admin/contributionManagement";
            } else if (isManager) {
                List<Article> articles = this.articleService.findAll();

                List<Article> approvedArticle = new ArrayList<>();

                String facultyName = null;

                User coordinatorUser = null;

                for (Article article : articles) {

                    if (article.isStatus())
                        approvedArticle.add(article);

                    facultyName =  article.getUser().getFaculty().getName();
                    Long userId = this.userService.findUserByFacultyAndRole(facultyName);
                    coordinatorUser = this.userService.findById(userId);

                }
                model.addAttribute("articles", approvedArticle);
                model.addAttribute("coordinatorUser", coordinatorUser);

                return "User/manager/approvalArticleManagement";

            } else {
                return "User/student/contributionManagement";//show error page
            }
        }else {
            return null; //show error page
        }

    }

    @GetMapping("/admin/showDetail")
        public String showdetail(@PathParam("id") Long id, Model model){
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "User/admin/ViewdetailContribution";
    }


    @GetMapping("/manager/detail_approval")
    public String showDetail(@PathParam("id") Long id,  Model model){
        Article article = this.articleService.findById(id);

        model.addAttribute("article", article);

        return "User/manager/viewdetailApproval";
    }



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
    public String addArticle(@ModelAttribute("article") Article article, @RequestParam("files")MultipartFile wordFile, @RequestParam("image") MultipartFile imageFile ) throws IOException {

        String wordFileName = StringUtils.cleanPath(wordFile.getOriginalFilename());
        String imageFileName = StringUtils.cleanPath(imageFile.getOriginalFilename());

        //set file into article
        article.setFileName(wordFileName);
        article.setImageArticle(imageFileName);
        this.articleService.save(article);

        //word file
        String uploadDirectory = DIRECTORY;
        FileUpload.saveFile(uploadDirectory, wordFileName, wordFile);

        //image file
        String imageDirectory = "src/main/resources/static/articleImage/" + article.getId();
        FileUpload.saveFile(imageDirectory, imageFileName, imageFile);

        this.articleService.save(article);

        this.articleService.saveAndUpdate(article);

//        article.getUser().getFaculty().getName();

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
        model.addAttribute("article", article);
        return "User/student/addArticle";
    }



}
