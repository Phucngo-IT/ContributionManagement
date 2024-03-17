package spring.boot.contributionmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.boot.contributionmanagement.entities.LogDownload;

@Repository
public interface LogDownloadRepository extends JpaRepository<LogDownload, Long> {
}
