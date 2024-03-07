package spring.boot.contributionmanagement.services;

import spring.boot.contributionmanagement.entities.Faculty;
import spring.boot.contributionmanagement.entities.Role;

import java.util.List;

public interface RoleService {
    public List<Role> findAll();

    public Role findById(Long id);

    public void saveAndUpdate(Role role);

    public void deleteById(Long id);
}
