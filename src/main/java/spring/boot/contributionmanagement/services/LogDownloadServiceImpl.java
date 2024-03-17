package spring.boot.contributionmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.contributionmanagement.entities.LogDownload;
import spring.boot.contributionmanagement.repositories.LogDownloadRepository;

import java.util.List;

@Service
public class LogDownloadServiceImpl implements LogDownloadService {
    private final LogDownloadRepository selectedContributionRepository;

    @Autowired
    public LogDownloadServiceImpl(LogDownloadRepository selectedContributionRepository) {
        this.selectedContributionRepository = selectedContributionRepository;
    }


    @Override
    public List<LogDownload> findAll() {
        return this.selectedContributionRepository.findAll();
    }

    @Override
    public LogDownload findById(Long id) {
        return this.selectedContributionRepository.findById(id).get();
    }

    @Override
    public void saveAndUpdate(LogDownload selectedContribution) {
        this.selectedContributionRepository.saveAndFlush(selectedContribution);
    }

    @Override
    public void deleteById(Long id) {
        this.selectedContributionRepository.deleteById(id);
    }
}
