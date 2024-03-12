package spring.boot.contributionmanagement.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import spring.boot.contributionmanagement.entities.SelectedContribution;
import spring.boot.contributionmanagement.entities.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    public User findByUsername(String username);
    //security

    public List<User> findAll();

    public User findById(Long id);

    public void save (User user);

    public void saveAndUpdate(User user);

    public void deleteById(Long id);
}
