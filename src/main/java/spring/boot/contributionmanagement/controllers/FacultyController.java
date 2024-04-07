package spring.boot.contributionmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
        model.addAttribute("faculties", faculty);
        return "User/admin/facultyManagement";

    }

    @GetMapping("/showForm")
    public String showFormFaculty(@ModelAttribute("title") String title,@ModelAttribute("error")String error,Model model){
        model.addAttribute("faculty", new Faculty());
        model.addAttribute("error", error);
        model.addAttribute("title", (title.isEmpty()) ? "add" : title);
        return "User/admin/addFaculty";
    }
    //
    @PostMapping("/save")
    public String addFaculty(Model model,@RequestParam("title")String title,@ModelAttribute("faculty") Faculty faculty,RedirectAttributes redirectAttributes ){
        String errors = "";
        if (faculty.getName().isEmpty()) {
            errors += "Name faculty is not blank!<br>";
        }
        if(errors!=""){
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("error", errors);
            model.addAttribute("title",title);
            System.out.println("save: "+ title) ;
            if(title.equals("add")){
                return "redirect:/faculty/showForm";
            }
            else {
                return "redirect:/faculty/update?id="+faculty.getId();
            }
        }
        else {
            this.facultyService.saveAndUpdate(faculty);
            return "redirect:/faculty";
        }
    }

    @GetMapping("/delete")
    public String deleteFaculty(@RequestParam("id")Long id){
        this.facultyService.deleteById(id);
        return "redirect:/faculty";
    }
    @GetMapping("/update")
    public String updateFaculty(@ModelAttribute("error")String error,@RequestParam("id")Long id, Model model){
        Faculty faculty = this.facultyService.findById(id);
        model.addAttribute("faculty", faculty);
        model.addAttribute("error", error);
        model.addAttribute("title", "update");
        return "User/admin/addFaculty";
    }

}
