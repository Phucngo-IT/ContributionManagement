package spring.boot.contributionmanagement.services;

import spring.boot.contributionmanagement.entities.AcademicYear;

import java.util.List;

public interface AcademicYearService {

    public List<AcademicYear> findAll();

    public AcademicYear findById(Long id);

    public void saveAndUpdate(AcademicYear academicYear);

    public void deleteById(Long id);
}
