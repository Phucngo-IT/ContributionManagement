package spring.boot.contributionmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.repositories.ArticleRepository;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
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
    public void deleteById(Long id) {
        this.articleRepository.deleteById(id);
    }
}
