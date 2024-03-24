package spring.boot.contributionmanagement.mailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.UserService;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final UserService userService;
    @Autowired
    public MailService(JavaMailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }

    public void sendMail(String mail, MailStructure mailStructure){
        //Get email address of user logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findByUsername(authentication.getName());
        String emailAddress = user.getEmail();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailAddress);
        simpleMailMessage.setSubject(mailStructure.getSubject());
        simpleMailMessage.setText(user.getFullName()+" "+ mailStructure.getMessage());
        simpleMailMessage.setTo(mail);
        mailSender.send(simpleMailMessage);

    }

}
