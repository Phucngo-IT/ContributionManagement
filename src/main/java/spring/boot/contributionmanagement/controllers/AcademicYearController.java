package spring.boot.contributionmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.boot.contributionmanagement.entities.AcademicYear;
import spring.boot.contributionmanagement.services.AcademicYearService;

import java.util.List;

@Controller
@RequestMapping("/academic_year")
public class AcademicYearController {
    private final AcademicYearService academicYearService;

    @Autowired
    public AcademicYearController(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @GetMapping
    public String list(Model model){
        List<AcademicYear> academic_year = this.academicYearService.findAll();
        model.addAttribute("academic_year", academic_year);
        return "User/admin/magazineManagement";
    }

    @GetMapping("/showForm")
    public String showFormAcademic(Model model){
        model.addAttribute("academic_year", new AcademicYear());
        return "User/admin/addAcademic";
    }
//
    @PostMapping("/save")
    public String addAcademic(@ModelAttribute("academic_year") AcademicYear academic_year){
        this.academicYearService.saveAndUpdate(academic_year);
        return "redirect:/admin/academicList";
    }

    @GetMapping("/delete")
    public String deleteAcademic(@RequestParam("id")Long id){
        this.academicYearService.deleteById(id);
        return "redirect:/admin/academicList";
    }
    @GetMapping("/update")

    public String updateAcademic(@RequestParam("id")Long id, Model model){
        AcademicYear academic_year = this.academicYearService.findById(id);
        model.addAttribute("academic_year", academic_year);
        return "User/admin/addAcademic";
    }



}
