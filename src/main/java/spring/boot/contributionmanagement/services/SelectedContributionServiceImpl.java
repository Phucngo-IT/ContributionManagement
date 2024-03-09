package spring.boot.contributionmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.contributionmanagement.entities.SelectedContribution;
import spring.boot.contributionmanagement.repositories.SelectedContributionRepository;

import java.util.List;

@Service
public class SelectedContributionServiceImpl implements SelectedContributionService {
    private final SelectedContributionRepository selectedContributionRepository;

    @Autowired
    public SelectedContributionServiceImpl(SelectedContributionRepository selectedContributionRepository) {
        this.selectedContributionRepository = selectedContributionRepository;
    }


    @Override
    public List<SelectedContribution> findAll() {
        return this.selectedContributionRepository.findAll();
    }

    @Override
    public SelectedContribution findById(Long id) {
        return this.selectedContributionRepository.findById(id).get();
    }

    @Override
    public void saveAndUpdate(SelectedContribution selectedContribution) {
        this.selectedContributionRepository.saveAndFlush(selectedContribution);
    }

    @Override
    public void deleteById(Long id) {
        this.selectedContributionRepository.deleteById(id);
    }
}
