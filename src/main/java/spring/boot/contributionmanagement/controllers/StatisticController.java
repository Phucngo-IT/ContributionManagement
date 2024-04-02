
package spring.boot.contributionmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.boot.contributionmanagement.entities.*;
import spring.boot.contributionmanagement.services.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class StatisticController {

    private final AcademicYearService academicYearService;
    private final FacultyService facultyService;
    private final ArticleService articleService;
    private final UserService userService;
    @Autowired
    public StatisticController(AcademicYearService academicYearService
            , FacultyService facultyService, ArticleService articleService, UserService userService){
        this.academicYearService =academicYearService;
        this.facultyService=facultyService;
        this.userService = userService;
        this.articleService = articleService;
    }
    @GetMapping("/statisticManagement")
    public String view(Model model){
        List<Article> allContributions = articleService.findAll();
//        List<Article> selectedContributions = allContributions.stream()
//                .filter(article->article.getStatus()== Article.Status.approved) // Lọc ra các bài báo đã được phê duyệt
//                .collect(Collectors.toList()); // Thu thập vào một danh sách mới

        Map<String, Integer> articleCountByYear = new HashMap<>();
        Map<String, Set<String>> facultyByYear = new HashMap<>();
        Map<String, Map<String, Integer>> facultyContributions = new HashMap<>(); // Tạo đối tượng facultyContributions
        Map<String, Map<String, Set<String>>> facultyContributors = new HashMap<>();


        List<Article> articleListFinal = new ArrayList<>();
        for (Article contribution : allContributions) {
            Article article = articleService.findById(contribution.getId());

            if (article != null) {
                AcademicYear academicYear = article.getAcademicYear();
                Faculty faculty = article.getUser().getFaculty();
                String studentName = article.getUser().getUsername(); // Assume student name is stored in User entity
                if (academicYear != null) {
                    String closure = academicYear.getClosureDate().toString().substring(0, 4);
                    String finalClosure = academicYear.getFinalClosureDate().toString().substring(0, 4);
                    String concatenatedYear = closure + "-" + finalClosure;
                    articleCountByYear.put(concatenatedYear, articleCountByYear.getOrDefault(concatenatedYear, 0) + 1);
                    if (faculty != null && studentName!=null) {
                        Set<String> fas = facultyByYear.getOrDefault(concatenatedYear, new HashSet<>());
                        fas.add(faculty.getName());
                        facultyByYear.put(concatenatedYear, fas);

                        // Cập nhật số lượng đóng góp cho từng khoa trong từng năm
                        Map<String, Integer> facultyContributionMap = facultyContributions.getOrDefault(concatenatedYear, new HashMap<>());
                        int contributionCount = facultyContributionMap.getOrDefault(faculty.getName(), 0);
                        facultyContributionMap.put(faculty.getName(), contributionCount + 1);
                        facultyContributions.put(concatenatedYear, facultyContributionMap);

                        // Cập nhật số lượng người đóng góp cho từng khoa trong từng năm
                        // Khởi tạo hoặc lấy ra map cho năm học được nối
                        Map<String, Set<String>> yearFacultyContributors = facultyContributors.getOrDefault(concatenatedYear, new HashMap<>());
                        // Khởi tạo hoặc lấy ra set cho khoa
                        Set<String> facultyStudents = yearFacultyContributors.getOrDefault(faculty.getName(), new HashSet<>());
                        // Thêm tên sinh viên vào set cho khoa
                        facultyStudents.add(studentName);
                        // Cập nhật set cho khoa trong map cho năm học được nối
                        yearFacultyContributors.put(faculty.getName(), facultyStudents);
                        // Cập nhật map cho năm học được nối trong map chính
                        facultyContributors.put(concatenatedYear, yearFacultyContributors);
                    }
                }
            }
        }

        // Kiểm tra và thêm tất cả các khoa vào facultyContributions
//        for (Map<String, Integer> contributionMap : facultyContributions.values()) {
//            for (Set<String> facultyNames : facultyByYear.values()) {
//                for (String facultyName : facultyNames) {
//                    if (!contributionMap.containsKey(facultyName)) {
//                        contributionMap.put(facultyName, 0);
//                    }
//                }
//            }
//        }
//        Long allContributor = allContributions.stream()
//                .count();

//        System.out.println("Faculty Contributions: " + facultyContributions);
//        System.out.println(facultyContributions.keySet()); //year
        model.addAttribute("facultyContributors", facultyContributors);
        model.addAttribute("facultyContributions", facultyContributions); // Đưa facultyContributions vào model
        return "User/manager/statisticManagement";
    }

        @GetMapping("statisticGuest")
        public String statisticGuest(Model model){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userAuth = (UserDetails) authentication.getPrincipal();
            String username = userAuth.getUsername();
            User user = this.userService.findByUsername(username);
            String facultyName = user.getFaculty().getName();
            List<Article> articles = articleService.findAll();
            Long allApproved = articles.stream()
                    .filter(article -> article.getStatus().equals(Article.Status.approved))
                    .count();
            long allRecheck = articles.stream()
                    .filter(article -> article.getStatus().equals(Article.Status.recheck))
                    .count();
                    System.out.println(allRecheck);
            System.out.println(allApproved);


//            List<Article> allApproved = articleService.findAllApprovedArticle("approved");
//            List<Article> allRecheck = articleService.findAllApprovedArticle("recheck");
            model.addAttribute("approved",allApproved);
            model.addAttribute("recheck",allRecheck);
            model.addAttribute("faculty", facultyName);
        return "User/guest/guestStatistic";
        }




}
