//package spring.boot.contributionmanagement.controllers;
//
//import org.hibernate.mapping.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import spring.boot.contributionmanagement.entities.AcademicYear;
//import spring.boot.contributionmanagement.entities.Article;
//import spring.boot.contributionmanagement.entities.Faculty;
//import spring.boot.contributionmanagement.services.AcademicYearService;
//import spring.boot.contributionmanagement.services.ArticleService;
//import spring.boot.contributionmanagement.services.FacultyService;
//import spring.boot.contributionmanagement.services.UserService;
//
//import java.util.*;
//
//@Controller
//public class StatisticController {
//
//        private final AcademicYearService academicYearService;
////        private final SelectedContributionService selectedContributionService;
//        private final FacultyService facultyService;
//        private final ArticleService articleService;
//        private final UserService userService;
//        @Autowired
//        public StatisticController( AcademicYearService academicYearService
//                , FacultyService facultyService, ArticleService articleService, UserService userService){
//            this.academicYearService =academicYearService;
//            this.facultyService=facultyService;
//            this.userService = userService;
//            this.articleService = articleService;
//        }
//        @GetMapping("/statistic")
//        public String view(Model model){
<<<<<<< HEAD
//            Map<String, Set<String>> facultyByYear = new HashMap            Map<,String Integer> articleCountByYear = new HashMap<>();
//<>();
=======
//            Map<String, Integer> articleCountByYear = new HashMap<>();
//            Map<String, Set<String>> facultyByYear = new HashMap<>();
>>>>>>> 558437d26aeb0f7ecfc11599fb8959de705d0385
//            Map<String, Map<String, Integer>> facultyContributions = new HashMap<>(); // Tạo đối tượng facultyContributions
//
//            List<Article> articleListFinal = new ArrayList<>();
//            for (Article article: articleListFinal) {
//                if (article.isStatus()){
//                    Article approvedArticle = articleService.findById(contribution.getArticle().getId());
//                }
//
//                if (article != null) {
//                    AcademicYear academicYear = article.getAcademicYear();
//                    Faculty faculty = article.getUser().getFaculty();
//                    if (academicYear != null) {
//                        String closure = academicYear.getClosureDate().toString().substring(0, 4);
//                        String finalClosure = academicYear.getFinalClosureDate().toString().substring(0, 4);
//                        String concatenatedYear = closure + "-" + finalClosure;
//                        articleCountByYear.put(concatenatedYear, articleCountByYear.getOrDefault(concatenatedYear, 0) + 1);
//                        if (faculty != null) {
//                            Set<String> fas = facultyByYear.getOrDefault(concatenatedYear, new HashSet<>());
//                            fas.add(faculty.getName());
//                            facultyByYear.put(concatenatedYear, fas);
//
//                            // Cập nhật số lượng đóng góp cho từng khoa trong từng năm
//                            Map<String, Integer> facultyContributionMap = facultyContributions.getOrDefault(concatenatedYear, new HashMap<>());
//                            int contributionCount = facultyContributionMap.getOrDefault(faculty.getName(), 0);
//                            facultyContributionMap.put(faculty.getName(), contributionCount + 1);
//                            facultyContributions.put(concatenatedYear, facultyContributionMap);
//                        }
//                    }
//                }
//            }
//
//            // Kiểm tra và thêm tất cả các khoa vào facultyContributions
//            for (Map<String, Integer> contributionMap : facultyContributions.values()) {
//                for (Set<String> facultyNames : facultyByYear.values()) {
//                    for (String facultyName : facultyNames) {
//                        if (!contributionMap.containsKey(facultyName)) {
//                            contributionMap.put(facultyName, 0);
//                        }
//                    }
//                }
//            }
//
////        System.out.println("Faculty Contributions: " + facultyContributions);
////        System.out.println(facultyContributions.keySet()); //year
//
//            model.addAttribute("facultyContributions", facultyContributions); // Đưa facultyContributions vào model
//            return "User/manager/statisticManagement";
//        }
//
//
//
//}
