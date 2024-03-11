package spring.boot.contributionmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.UserService;

@SpringBootApplication
public class ContributionManagementApplication {


    public static void main(String[] args) {
		SpringApplication.run(ContributionManagementApplication.class, args);
	}
}
