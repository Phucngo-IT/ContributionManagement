package spring.boot.contributionmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.boot.contributionmanagement.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
