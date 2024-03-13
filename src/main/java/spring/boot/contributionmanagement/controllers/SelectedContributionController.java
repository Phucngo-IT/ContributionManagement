package spring.boot.contributionmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.boot.contributionmanagement.entities.*;
import spring.boot.contributionmanagement.services.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SelectedContributionController {
    private final SelectedContributionService selectedContributionService;
    private final ArticleService articleService;
    private final FacultyService facultyService;
    private final UserService userService;
    private final AcademicYearService academicYearService;

    @Autowired
    public SelectedContributionController(SelectedContributionService selectedContributionService,
    ArticleService articleService, FacultyService facultyService, AcademicYearService academicYearService
    ,UserService userService){
        this.selectedContributionService = selectedContributionService;
        this.articleService = articleService;
        this.facultyService =  facultyService;
        this.academicYearService =  academicYearService;
        this.userService=userService;
    }


//    @GetMapping("/selectedContributionList")
//    public String list(Model model){
//        List<SelectedContribution> selected = this.selectedContributionService.findAll();
//
//        List<Article> articles = this.articleService.findAll();
//        List<Faculty> faculties = this.facultyService.findAll();
//        List<AcademicYear> academicYears = this.academicYearService.findAll();
//        List<User> users = this.userService.findAll();
//        model.addAttribute("users", users);
//        model.addAttribute("academicYears", academicYears);
//        model.addAttribute("articles", articles);
//        model.addAttribute("faculties", faculties);
//        model.addAttribute("selected", selected);
//        return "User/approvedArticlesList";
//    }


    @GetMapping("/selectedContributionList")
    public String list(Model model){
        List<SelectedContribution> selected = this.selectedContributionService.findAll();

        // Tạo một danh sách mới để lưu thông tin của các quyển sách đã chọn
        List<Article> selectedArticles = new ArrayList<>();

        // Lặp qua danh sách SelectedContribution để lấy thông tin từ các bảng liên quan
        for (SelectedContribution contribution : selected) {
            // Lấy thông tin về bài báo dựa trên khóa ngoại của SelectedContribution
            Article article = this.articleService.findById(contribution.getId());
            selectedArticles.add(article);
        }

        // Lấy danh sách các thông tin khác cần thiết
        List<Faculty> faculties = this.facultyService.findAll();
        List<AcademicYear> academicYears = this.academicYearService.findAll();
        List<User> users = this.userService.findAll();

        // Truyền thông tin vào model
        model.addAttribute("users", users);
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("selectedArticles", selectedArticles); // Thêm danh sách các bài báo đã chọn vào model
        model.addAttribute("faculties", faculties);
        model.addAttribute("selected", selected);
        return "User/approvedArticlesList";
    }

}
