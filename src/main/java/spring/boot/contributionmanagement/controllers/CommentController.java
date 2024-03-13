package spring.boot.contributionmanagement.controllers;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.boot.contributionmanagement.entities.Comment;
import spring.boot.contributionmanagement.services.CommentService;

import java.util.List;


@Controller
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;


    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;

    }
    @GetMapping
    public String show(Model model){
    List<Comment> comment= this.commentService.findAll();
    model.addAttribute("comments",comment);
    return "User/coordinator/feedbackManagement";
    }
}
