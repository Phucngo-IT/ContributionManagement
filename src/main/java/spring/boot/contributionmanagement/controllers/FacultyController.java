package spring.boot.contributionmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.boot.contributionmanagement.entities.Faculty;
import spring.boot.contributionmanagement.services.FacultyService;

import java.util.List;
@Controller
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService){
        this.facultyService = facultyService;
    }
    @GetMapping
    public String listFaculty(Model model){
        List<Faculty> faculty = this.facultyService.findAll();
        model.addAttribute("faculty", faculty);
        return "User/admin/facultyList";
    }

    @GetMapping("/showForm")
    public String showFormFaculty(Model model){
        model.addAttribute("faculty", new Faculty());
        return "User/admin/addFaculty";
    }
    //
    @PostMapping("/save")
    public String addFaculty(@ModelAttribute("faculty") Faculty faculty){
        this.facultyService.saveAndUpdate(faculty);
        return "redirect:/faculty";
    }

    @GetMapping("/delete")
    public String deleteFaculty(@RequestParam("id")Long id){
        this.facultyService.deleteById(id);
        return "redirect:/faculty";
    }
    @GetMapping("/update")
    public String updateFaculty(@RequestParam("id")Long id, Model model){
        Faculty faculty = this.facultyService.findById(id);
        model.addAttribute("faculty", faculty);
        return "User/admin/addFaculty";
    }

}
