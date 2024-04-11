package spring.boot.contributionmanagement.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.repositories.ArticleRepository;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final EntityManager entityManager;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, EntityManager entityManager) {
        this.articleRepository = articleRepository;
        this.entityManager = entityManager;
    }
//

    @Override
    public List<Article> findAll() {
        return this.articleRepository.findAll();
    }

    @Override
    public Article findById(Long id) {
        return this.articleRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveAndUpdate(Article article) {
        this.articleRepository.saveAndFlush(article);
    }

    @Override
    @Transactional
    public void save(Article article) {
        this.articleRepository.save(article);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.articleRepository.deleteById(id);
    }

    @Override
    public List<Article> findAllApprovedArticle(String status) {
        String jpqlQuery = "Select a from Article a JOIN FETCH a.comments and JOIN FETCH a.user where a.status=:status";

        TypedQuery<Article> query = this.entityManager.createQuery(jpqlQuery, Article.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<Article> findAllByFacultyName(String facultyName) {
        String jpqlQuery = "SELECT a FROM Article a WHERE a.user.faculty.name = :facultyName";
        TypedQuery<Article> query = entityManager.createQuery(jpqlQuery, Article.class);
        query.setParameter("facultyName", facultyName);
        return query.getResultList();
    }

}
