package spring.boot.contributionmanagement.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.boot.contributionmanagement.entities.AcademicYear;
import spring.boot.contributionmanagement.services.AcademicYearService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public String showFormAcademic(@ModelAttribute("titlePage") String titlePage,@ModelAttribute("error") String error, Model model){
        model.addAttribute("academic_year", new AcademicYear());
        model.addAttribute("error", error);
        model.addAttribute("titlePage", (titlePage.isEmpty()) ? "add" : titlePage);
        return "User/admin/Addacademicyear";
    }
//
    @PostMapping("/save")
    public String addAcademic(Model model,@RequestParam("titlePage") String titlePage, RedirectAttributes redirectAttributes, @ModelAttribute("academic_year") AcademicYear academic_year){
        LocalDate currentDate = (LocalDate.now());
        Date sqlCurrentDate = java.sql.Date.valueOf(currentDate);
        Date closureDate = academic_year.getClosureDate();
        Date finalClosureDate = academic_year.getFinalClosureDate();
        String errors = "";
        System.out.println("save: "+titlePage);
//        System.out.println(request.getPathInfo());

//        if(academic_year.getClosureDate().equals("")||academic_year.getFinalClosureDate().equals("")){
//            errors += "Clousure date is not blank!<br>";
//        }
        if(academic_year.getTitle().isEmpty()){
            errors += "Title is not blank!<br>";
        }
        if(academic_year.getDiscription().isEmpty()){
            errors += "Discription is not blank!<br>";
        }
        if (closureDate.before(sqlCurrentDate) || finalClosureDate.before(sqlCurrentDate)) {
            errors += "Closure Date and Final Closure Date of academic year must be add after current date!<br>";
        }
        if (finalClosureDate.before(closureDate)) {
            errors += "Final Closure Date of academic year must be add after current date and cloure date!<br>";
        }

        if(errors!=""){
            System.out.println("Error");
            System.out.println("Save: "+ titlePage);
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("error", errors);
            model.addAttribute("titlePage",titlePage);
//            if(currentUrl.equals("http://localhost:8080/academic_year/update?id=5")){
//
//                return "redirect:/academic_year/update?id="+;
//            }
            if(titlePage.equals("add")){
                return "redirect:/academic_year/showForm";
            }
            else {
                return "redirect:/academic_year/update?id="+academic_year.getId();
            }
        }
        else {
            this.academicYearService.saveAndUpdate(academic_year);
            return "redirect:/academic_year";
        }
    }

    @GetMapping("/delete")
    public String deleteAcademic(@RequestParam("id")Long id){
        this.academicYearService.deleteById(id);
        return "redirect:/academic_year";
    }
    @GetMapping("/update")
    public String updateAcademic(@ModelAttribute("error")String error,@RequestParam("id")Long id, Model model){
        AcademicYear academic_year = this.academicYearService.findById(id);
        model.addAttribute("academic_year", academic_year);
        model.addAttribute("error", error);
        model.addAttribute("titlePage", "update");
        return "User/admin/Addacademicyear";
    }



}
