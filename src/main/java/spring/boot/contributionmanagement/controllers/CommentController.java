package spring.boot.contributionmanagement.controllers;

import jakarta.websocket.server.PathParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.entities.Comment;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.ArticleService;
import spring.boot.contributionmanagement.services.CommentService;
import spring.boot.contributionmanagement.services.RoleService;
import spring.boot.contributionmanagement.services.UserService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Controller
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final RoleService roleService;

    private final ArticleService articleService;


    @Autowired
    public CommentController(CommentService commentService, UserService userService, RoleService roleService, ArticleService articleService) {
        this.commentService = commentService;
        this.userService = userService;
        this.roleService = roleService;
        this.articleService = articleService;
    }
    @GetMapping
    public String show(@PathParam("id") Long id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();

        if (user != null) {

            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            boolean isStudent = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_STUDENT"));
            boolean isCoordinator = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_COORDINATOR"));
            boolean isManager = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

            if (isStudent){
                Article article = this.articleService.findById(id);
                //convert date from sql date to local date
                LocalDate uploadDate = article.getUploadDate().toLocalDate();
                LocalDate over14Days = uploadDate.plusDays(14);//plus more 14 days
                LocalDate currentDate = LocalDate.now();

                List<Comment> comments = article.getComments();
                model.addAttribute("curentDate", currentDate);
                model.addAttribute("uploadDate", over14Days);
                model.addAttribute("article", article);
                model.addAttribute("comments", comments);
                model.addAttribute("comment", new Comment());
                return "User/student/detailFeedback";

            } else if (isCoordinator) {
                Article article = this.articleService.findById(id);
                //convert date from sql date to local date
                LocalDate uploadDate = article.getUploadDate().toLocalDate();
                LocalDate over14Days = uploadDate.plusDays(14);//plus more 14 days

                LocalDate currentDate = LocalDate.now();
                List<Comment> comments = article.getComments();
                model.addAttribute("curentDate", currentDate);
                model.addAttribute("uploadDate", over14Days);
                model.addAttribute("article", article);
                model.addAttribute("comments", comments);
                model.addAttribute("comment", new Comment());
                return "User/coordinator/feedbackContent";
            }else if (isManager) {
                Article article = this.articleService.findById(id);

                List<Comment> comments = article.getComments();

                model.addAttribute("article", article);
                model.addAttribute("comments", comments);
                return "User/manager/viewdetailApproval";
            }else {
                return null;
            }

        }else{
            return null;
        }

    }
    @PostMapping("/save/{id}")
    public String save(@PathVariable("id") Long id, @ModelAttribute("comment") Comment comment, Model model){


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        //get User by username
        User userObj = this.userService.findByUsername(user.getUsername());
        //get
        Article article = this.articleService.findById(id);


        LocalDate getCurrentDate = LocalDate.now();

        comment.setDateComment(Date.valueOf(getCurrentDate));
        comment.setUser(userObj);
        comment.setArticle(article);

        this.commentService.saveAndUpdate(comment);

        return "redirect:/article";
    }


    @PostMapping("/approve/{id}")
    public String approve(@PathVariable("id") Long id,@RequestParam("action")String action ,RedirectAttributes redirectAttributes ){
        Article article = articleService.findById(id);
        if (article != null) {
            if(action.equals("approve")){
                article.setStatus(Article.Status.approved);
                articleService.save(article);
            }
            else {
                article.setStatus(Article.Status.reject);
                articleService.save(article);
            }
            return "redirect:/article";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "The article dose not exist!");
            return "redirect:/article";
        }
    }

}





