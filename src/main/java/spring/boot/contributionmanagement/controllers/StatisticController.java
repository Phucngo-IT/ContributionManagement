package spring.boot.contributionmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.boot.contributionmanagement.entities.*;
import spring.boot.contributionmanagement.services.*;

import java.util.List;

@Controller
public class StatisticController {

    private final AcademicYearService academicYearService;
    private final SelectedContributionService selectedContributionService;
    private final FacultyService facultyService;
    private final ArticleService articleService;
    private final UserService userService;
    @Autowired
    public StatisticController(SelectedContributionService selectedContributionService, AcademicYearService academicYearService
    , FacultyService facultyService, ArticleService articleService, UserService userService){
        this.selectedContributionService =selectedContributionService;
        this.academicYearService =academicYearService;
        this.facultyService=facultyService;
        this.userService = userService;
        this.articleService = articleService;
    }
    @GetMapping("/statistic")
    public String view(Model model){
        List<SelectedContribution>selectedContributions = selectedContributionService.findAll();
        List<Faculty> faculties = facultyService.findAll();
        List<AcademicYear> academicYears =  academicYearService.findAll();


        return "User/admin/statistic";
    }
}
