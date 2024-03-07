package spring.boot.contributionmanagement.services;

import spring.boot.contributionmanagement.entities.SelectedContribution;
import spring.boot.contributionmanagement.entities.User;

import java.util.List;

public interface UserService {
    public List<User> findAll();

    public User findById(Long id);

    public void saveAndUpdate(User user);

    public void deleteById(Long id);
}
