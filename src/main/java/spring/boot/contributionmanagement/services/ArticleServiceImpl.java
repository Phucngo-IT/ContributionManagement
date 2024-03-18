package spring.boot.contributionmanagement.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public void saveAndUpdate(Article article) {
        this.articleRepository.saveAndFlush(article);
    }

    @Override
    public void save(Article article) {
        this.articleRepository.save(article);
    }

    @Override
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
}
