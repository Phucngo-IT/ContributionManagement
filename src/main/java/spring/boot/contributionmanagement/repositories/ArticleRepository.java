package spring.boot.contributionmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.boot.contributionmanagement.entities.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
