package spring.boot.contributionmanagement.mailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.services.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final UserService userService;
    private final Map<String, String> otpStorage = new HashMap<>();
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

//    generate a random otp
    public String generateOtp(){
        Random random  = new Random();
        int otpLenght = 6;
        StringBuilder otp = new StringBuilder();

        for(int i = 0; i < otpLenght; i++){
            otp.append(random.nextInt(10));// Generate a random digit between 0 and 9
        }
        return otp.toString();
    }


    public void sendOtp(String email){
        //Generate OTP
        String otp = generateOtp();
        otpStorage.put(email, otp);//save the email and otp into Map

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("universityofgreenwich@gmail.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Your OTP for verification");
        simpleMailMessage.setText("Your OTP is: " + otp);

        // Send email
        mailSender.send(simpleMailMessage);
    }

    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStorage.get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }




}



















