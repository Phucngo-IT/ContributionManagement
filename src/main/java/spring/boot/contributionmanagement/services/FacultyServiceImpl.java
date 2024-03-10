package spring.boot.contributionmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.contributionmanagement.entities.Faculty;
import spring.boot.contributionmanagement.repositories.FacultyRepository;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    @Override
    public List<Faculty> findAll() {
        return this.facultyRepository.findAll();
    }

    @Override
    public Faculty findById(Long id) {
        return this.facultyRepository.findById(id).get();
    }

    @Override
    public void saveAndUpdate(Faculty faculty) {
        this.facultyRepository.saveAndFlush(faculty);
    }

    @Override
    public void deleteById(Long id) {
        this.facultyRepository.deleteById(id);
    }
}
