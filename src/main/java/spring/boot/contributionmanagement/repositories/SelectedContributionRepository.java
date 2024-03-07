package spring.boot.contributionmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.boot.contributionmanagement.entities.SelectedContribution;

@Repository
public interface SelectedContributionRepository extends JpaRepository<SelectedContribution, Long> {
}
