package spring.boot.contributionmanagement.controllers;

import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    //show article with Student, Admin, Manager and Coordinator role
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
                String username = userAuth.getUsername();
                User user = this.userService.findByUsername(username);
                String facultyName = user.getFaculty().getName();
                List<Article> articles = this.articleService.findAll();
                List<Article> articlesWithFacultyAuths = new ArrayList<>();
                for (Article article:articles){
                    if (article.getUser().getFaculty().getName().equals(facultyName)){
                        articlesWithFacultyAuths.add(article);
                    }
                }
                model.addAttribute("articles", articlesWithFacultyAuths);
                return "User/coordinator/feedbackManagement";
            } else if (isAdmin) {
                List<Article> articles = this.articleService.findAll();
                model.addAttribute("articles", articles);
                return "User/admin/contributionManagement";
            } else if (isManager) {
                List<Article> articles = this.articleService.findAll();
                List<Article> approvedArticles = new ArrayList<>();
                List<String> fileNames = new ArrayList<>();//
                String facultyName = null;
                User coordinatorUser = null;
                for (Article article : articles) {
                    if (article.isStatus()) {
                        approvedArticles.add(article);
                        fileNames.add(article.getFileName());
//                        facultyName.add(article.getUser().getFaculty().getName());
//                        Long userId = this.userService.findUserByFacultyAndRole(facultyName);
//                        coordinatorUser = this.userService.findById(userId);
                    }
                }
                String fileNamesString = String.join(",", fileNames);//make ArrayList file name into a single string
                model.addAttribute("articles", approvedArticles);
//                model.addAttribute("coordinatorUser", coordinatorUser);
                model.addAttribute("fileNames", fileNamesString);
                return "User/manager/approvalArticleManagement";
            } else {
                return "redirect:/login/403";//show error page
            }
        }else {
            return "redirect:/login/404"; //show error page
        }
    }

    @GetMapping("/admin/showDetail")//admin
        public String showdetail(@PathParam("id") Long id, Model model){
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "User/admin/ViewdetailContribution";
    }


    @GetMapping("/manager/detail_approval")//manager
    public String showDetail(@PathParam("id") Long id,  Model model){
        Article article = this.articleService.findById(id);
        model.addAttribute("article", article);
        return "User/manager/viewdetailApproval";
    }

    @GetMapping("/showForm")
    public String showFormArticle(Model model, HttpServletRequest request, @ModelAttribute("error")String error){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currentDate = (LocalDate.now().format(formatter));
            List<AcademicYear> academicYears = academicYearService.findAll();
            Article article = new Article();
            article.setUploadDate(Date.valueOf(currentDate));
            article.setUser(userService.findByUsername(username));
            //                model.addAttribute("currentDate", currentDate);
            model.addAttribute("error", error);
            model.addAttribute("academicYears", academicYears);
            model.addAttribute("article", article);
            model.addAttribute("requestUri", request.getRequestURI());
            return "User/student/addArticle";
        } else {
            return "redirect:/login";
        }
    }
//    public String showFormArticle(Model model){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String username = userDetails.getUsername();
////                Long userId = userService.findUserIdByUsername(username);
////                model.addAttribute("userId", userId);
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String currentDate = (LocalDate.now().format(formatter));
//            List<AcademicYear> academicYears = academicYearService.findAll();
//            Article article = new Article();
//            article.setUploadDate(Date.valueOf(currentDate));
//            article.setUser(userService.findByUsername(username));
////                model.addAttribute("currentDate", currentDate);
//            model.addAttribute("academicYears", academicYears);
//            model.addAttribute("article", article);
//            return "User/student/addArticle";
//        } else {
//            // Chưa đăng nhập
//            return "redirect:/login"; // hoặc bất kỳ trang nào bạn muốn chuyển hướng đến
//        }
//    }
////    //

//    @PostMapping("/save")
//    public String addArticle(RedirectAttributes redirectAttributes, @ModelAttribute("article") Article article,
//                             @RequestParam("files")MultipartFile wordFile, @RequestParam("image") MultipartFile imageFile ) throws IOException {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate currentDate = LocalDate.now();
//        String finalClosureDateStr = article.getAcademicYear().getFinalClosureDate().toString();
//        LocalDate finalClosureDate = LocalDate.parse(finalClosureDateStr, formatter);
//        String errors = "";
//
//        if (finalClosureDate.isBefore(currentDate)) {
//            errors +=" Article must be submit before final closure date!";
//            redirectAttributes.addFlashAttribute("error",errors);
////                redirectAttributes.addFlashAttribute("error",errors);
//            System.out.println(finalClosureDate);
//            System.out.println("Error!!!!!!!!!");
////                return "redirect:/article?finalClosureDate=" + finalClosureDate;
//            return "redirect:/article/showForm";
//        } else {
//            String wordFileName = StringUtils.cleanPath(wordFile.getOriginalFilename());
//            String imageFileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
//            //set file into article
//            article.setFileName(wordFileName);
//            article.setImageArticle(imageFileName);
//            this.articleService.save(article);
//            //word file
//            String uploadDirectory = DIRECTORY;
//            FileUpload.saveFile(uploadDirectory, wordFileName, wordFile);
//            //image file
//            String imageDirectory = "src/main/resources/static/articleImage/" + article.getId();
//            FileUpload.saveFile(imageDirectory, imageFileName, imageFile);
//            this.articleService.save(article);
//            this.articleService.saveAndUpdate(article);
//            //get email address to send an email to coordinator of each faculty
//            String facultyName = article.getUser().getFaculty().getName();
//            Long userId = this.userService.findUserByFacultyAndRole(facultyName);
//            User user = this.userService.findById(userId);
//            String email = user.getEmail();
//            this.mailService.sendMail(email, mailStructure);
//            return "redirect:/article";
//        }
//    }
//

    @GetMapping("/delete")
    public String deleteArticle(@RequestParam("id")Long id){
        this.articleService.deleteById(id);
        return "redirect:/article";
    }

//
//
//    @GetMapping("/update")
//    public String updateArticle(@RequestParam("id")Long id, Model model,  HttpServletRequest request, @ModelAttribute("error")String error){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            Article article = this.articleService.findById(id);
//            String username = userDetails.getUsername();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String currentDate = (LocalDate.now().format(formatter));
//            List<AcademicYear> academicYears = academicYearService.findAll();
//            article.setUploadDate(Date.valueOf(currentDate));
//            article.setUser(userService.findByUsername(username));
////            article.setAcademicYear(article.getAcademicYear());
//            //                model.addAttribute("currentDate", currentDate);
//
//            model.addAttribute("academicYear", article.getAcademicYear());
//
//            model.addAttribute("error", error);
//            model.addAttribute("academicYears", academicYears);
////            redirectAttributes.addFlashAttribute("article",article);
//            model.addAttribute("article", article);
//            model.addAttribute("requestUri", request.getRequestURI());
//            return "User/student/updateArticle";
//        } else {
//            // Chưa đăng nhập
//            return "redirect:/login"; // hoặc bất kỳ trang nào bạn muốn chuyển hướng đến
//        }
//
//    }

    @GetMapping("/showFormUpdate")
    public String showFormUpdateArticle(@RequestParam("id")Long id, Model model, HttpServletRequest request, @ModelAttribute("error")String error) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Article article = this.articleService.findById(id);
            String username = userDetails.getUsername();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate currentDate = (LocalDate.now());
            java.sql.Date sqlCurrentDate = java.sql.Date.valueOf(currentDate);
            List<AcademicYear> academicYears = academicYearService.findAll();
            article.setUploadDate(Date.valueOf(currentDate));
            article.setStatus(article.isStatus());
            article.setUser(userService.findByUsername(username));
            System.out.println("showFormUpdate: "+article.isStatus());

            model.addAttribute("error", error);
            model.addAttribute("academicYears", academicYears);
            model.addAttribute("article", article);
            model.addAttribute("requestUri", request.getRequestURI());
            return "User/student/updateArticle";
        } else {
            // Chưa đăng nhập
            return "redirect:/login"; // hoặc bất kỳ trang nào bạn muốn chuyển hướng đến
        }
    }





    @PostMapping("/save")
    public String addArticle(@ModelAttribute("article") Article article, RedirectAttributes redirectAttributes, @RequestParam("files")MultipartFile wordFile, @RequestParam("image") MultipartFile imageFile ) throws IOException {
        LocalDate currentDate = (LocalDate.now());
        Date ClosureDate = (article.getAcademicYear().getClosureDate());
        Date sqlCurrentDate = java.sql.Date.valueOf(currentDate);
        String errors = "";

        if (ClosureDate.before(sqlCurrentDate)) {
            errors += " Article must be submit before closure date!<br>";
        }
        if(imageFile.isEmpty()){
            errors += " Article must be add image file before submit!<br>";

        }
        if(wordFile.isEmpty()){
            errors += " Article must be add word file before submit!<br>";
        }
        if(article.getTitle().isEmpty()){
            errors += " Article must be add title before submit!<br>";
        }
        if(article.getDiscription().isEmpty()){
            errors += " Article must be add discription before submit!<br>";
        }

        if(errors!=""){
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("error", errors);
            return "redirect:/article/showForm";
        }

        else {
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

            //get email address to send an email to coordinator of each faculty

            String facultyName = article.getUser().getFaculty().getName();
            Long userId = this.userService.findUserByFacultyAndRole(facultyName);
            User user = this.userService.findById(userId);
            String email = user.getEmail();
            this.mailService.sendMail(email, mailStructure);
            return "redirect:/article";

        }

    }



    @PostMapping("/saveUpdate")
    public String updateArticle(@ModelAttribute("article") Article article
            , RedirectAttributes redirectAttributes,
             @RequestParam("files")MultipartFile wordFile,
             @RequestParam("image") MultipartFile imageFile ) throws IOException {
//        System.out.println("0 Article: "+article.getId());
//        System.out.println(article.getAcademicYear());
//
//        System.out.println("0 Title: "+article.getTitle());
//        System.out.println("0 Status: "+article.isStatus());
//        System.out.println("0 id: "+article.getId());

        AcademicYear aca =academicYearService.findById(article.getAcademicYear().getId());
        LocalDate currentDate = (LocalDate.now());
//        System.out.println(aca);
//        LocalDate finalClosureDate = (article.getAcademicYear().getFinalClosureDate()).toLocalDate();
//        Date sqlFinalClosureDate= java.sql.Date.valueOf(finalClosureDate);
        Date sqlCurrentDate = java.sql.Date.valueOf(currentDate);

//        Date closureDate = (aca.getClosureDate());
        Date finalClosureDate = (aca.getFinalClosureDate());
//        java.sql.Date sqlClosureDate = new java.sql.Date(closureDate.getTime());
        java.sql.Date sqlFinalClosureDate = new java.sql.Date(finalClosureDate.getTime());
//        System.out.println("Article: "+article.getId());
//
//        System.out.println("Title: "+article.getTitle());
//        System.out.println("Status: "+article.isStatus());
//        System.out.println("id: "+article.getId());
//        Date sqlCurrentDate = java.sql.Date.valueOf(currentDate);
        String errors = "";

//        if(sqlClosureDate.before(sqlCurrentDate)) {
//            errors += " Article must be submit before closure date!<br>";
//        }
        System.out.println("0 " +article.isStatus());
        if(article.isStatus()){
            errors += "Article was aprroved by Coordinator so you couldn't edit!<br>";
        }
        if (sqlFinalClosureDate.before(sqlCurrentDate)) {
            errors += " Article must be edit before final closure date!<br>";
        }
        if(imageFile.isEmpty()){
            errors += " Article must be add image file before submit!<br>";

        }
        if(wordFile.isEmpty()){
            errors += " Article must be add word file before submit!<br>";
        }
        if(article.getTitle().isEmpty()){
            errors += " Article must be add title before submit!<br>";
        }
        if(article.getDiscription().isEmpty()){
            errors += " Article must be add discription before submit!<br>";
        }


        if(errors!=""){
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("error",errors);
            return "redirect:/article/showFormUpdate";
        }

        else {
            System.out.println("Title 2: "+article.getTitle());
            System.out.println("id 2: "+article.getId());
            String wordFileName = StringUtils.cleanPath(wordFile.getOriginalFilename());
            String imageFileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            //set file into article
            article.setStatus(article.isStatus());
            article.setFileName(wordFileName);
            article.setImageArticle(imageFileName);
            article.setAcademicYear(aca);
            this.articleService.save(article);
            this.articleService.saveAndUpdate(article);

            //word file
            String uploadDirectory = DIRECTORY;
            FileUpload.saveFile(uploadDirectory, wordFileName, wordFile);

            //image file
            String imageDirectory = "src/main/resources/static/articleImage/" + article.getId();
            FileUpload.saveFile(imageDirectory, imageFileName, imageFile);


//            this.articleService.saveAndUpdate(article);
            //get email address to send an email to coordinator of each faculty

            String facultyName = article.getUser().getFaculty().getName();
            Long userId = this.userService.findUserByFacultyAndRole(facultyName);
            User user = this.userService.findById(userId);
            String email = user.getEmail();
            this.mailService.sendMail(email, mailStructure);
            return "redirect:/article";
        }
    }


}
