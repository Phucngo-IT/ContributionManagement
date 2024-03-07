package spring.boot.contributionmanagement.services;

import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.entities.Faculty;

import java.util.List;

public interface FacultyService {
    public List<Faculty> findAll();

    public Faculty findById(Long id);

    public void saveAndUpdate(Faculty faculty);

    public void deleteById(Long id);
}
