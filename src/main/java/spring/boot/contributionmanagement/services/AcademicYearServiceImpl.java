package spring.boot.contributionmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.contributionmanagement.entities.AcademicYear;
import spring.boot.contributionmanagement.repositories.AcademicYearRepository;

import java.util.List;

@Service
public class AcademicYearServiceImpl implements AcademicYearService {
    private final AcademicYearRepository academicYearRepository;

    @Autowired
    public AcademicYearServiceImpl(AcademicYearRepository academicYearRepository) {
        this.academicYearRepository = academicYearRepository;
    }

    @Override
    public List<AcademicYear> findAll() {
        return this.academicYearRepository.findAll();
    }

    @Override
    public AcademicYear findById(Long id) {
        return this.academicYearRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveAndUpdate(AcademicYear academicYear) {
        this.academicYearRepository.saveAndFlush(academicYear);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.academicYearRepository.deleteById(id);
    }
}
