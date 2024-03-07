package spring.boot.contributionmanagement.services;

import spring.boot.contributionmanagement.entities.SelectedContribution;

import java.util.List;

public interface SelectedContributionService {
    public List<SelectedContribution> findAll();

    public SelectedContribution findById(Long id);

    public void saveAndUpdate(SelectedContribution selectedContribution);

    public void deleteById(Long id);
}
