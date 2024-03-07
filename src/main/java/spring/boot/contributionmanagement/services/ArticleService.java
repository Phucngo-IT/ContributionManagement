package spring.boot.contributionmanagement.services;
import spring.boot.contributionmanagement.entities.Article;

import java.util.List;

public interface ArticleService {
    public List<Article> findAll();

    public Article findById(Long id);

    public void saveAndUpdate(Article article);

    public void deleteById(Long id);
}
