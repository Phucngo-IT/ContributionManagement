package spring.boot.contributionmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.boot.contributionmanagement.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
