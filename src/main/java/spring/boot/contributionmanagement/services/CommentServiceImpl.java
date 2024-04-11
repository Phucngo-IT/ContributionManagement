package spring.boot.contributionmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.contributionmanagement.entities.Comment;
import spring.boot.contributionmanagement.repositories.CommentRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

//
    @Override
    public List<Comment> findAll() {
        return this.commentRepository.findAll();
    }

    @Override
    public Comment findById(Long id) {
        return this.commentRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveAndUpdate(Comment comment) {
        this.commentRepository.saveAndFlush(comment);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.commentRepository.deleteById(id);
    }
}
