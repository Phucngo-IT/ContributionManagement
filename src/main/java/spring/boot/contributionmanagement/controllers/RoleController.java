package spring.boot.contributionmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.boot.contributionmanagement.entities.Faculty;
import spring.boot.contributionmanagement.entities.Role;
import spring.boot.contributionmanagement.services.RoleService;

import java.util.List;

@Controller
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @GetMapping("/admin/roleList")
    public String list(Model model){
        List<Role> role = this.roleService.findAll();
        model.addAttribute("role", role);
        return "User/admin/roleList";
    }

    @GetMapping("/admin/showFormRole")
    public String showFormRole(Model model){
        model.addAttribute("role", new Role());
        return "User/admin/addRole";
    }
    //
    @PostMapping("/admin/addRole")
    public String adRole(@ModelAttribute("role") Role role){
        this.roleService.saveAndUpdate(role);
        return "redirect:/admin/roleList";
    }

    @GetMapping("/admin/deleteRole")
    public String deleteRole(@RequestParam("id")Long id){
        this.roleService.deleteById(id);
        return "redirect:/admin/roleList";
    }
    @GetMapping("/admin/updateRole")

    public String updateRole(@RequestParam("id")Long id, Model model){
        Role role = this.roleService.findById(id);
        model.addAttribute("role", role);
        return "User/admin/addRole";
    }
}
