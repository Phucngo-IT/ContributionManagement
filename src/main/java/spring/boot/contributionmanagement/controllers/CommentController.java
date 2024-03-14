package spring.boot.contributionmanagement.controllers;

import jakarta.websocket.server.PathParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.entities.Comment;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.ArticleService;
import spring.boot.contributionmanagement.services.CommentService;
import spring.boot.contributionmanagement.services.RoleService;
import spring.boot.contributionmanagement.services.UserService;

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
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails user = (UserDetails) authentication.getPrincipal();
//        //
//        if (user.getAuthorities() == roleService.findById(4L)){
//
//            List<Comment> comments = this.commentService.findAll();
//            model.addAttribute("comments", comments);
//            return "User/coordinator/feedbackManagement";
//        }else if (user.getAuthorities() == roleService.findById(2L)){
            Article article = this.articleService.findById(id);

            List<Comment> comments = article.getComments();


            model.addAttribute("comments",comments);
            return "User/student/detailFeedback";
//        }else {
//            return null;
//        }
//        return "User/student/detailFeedback";
    }


}
