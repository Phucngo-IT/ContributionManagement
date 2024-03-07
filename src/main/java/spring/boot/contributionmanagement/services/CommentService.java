package spring.boot.contributionmanagement.services;

import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.entities.Comment;

import java.util.List;

public interface CommentService {
    public List<Comment> findAll();

    public Comment findById(Long id);

    public void saveAndUpdate(Comment comment);

    public void deleteById(Long id);
}
