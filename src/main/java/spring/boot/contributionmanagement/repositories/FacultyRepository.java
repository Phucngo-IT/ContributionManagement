package spring.boot.contributionmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.boot.contributionmanagement.entities.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
