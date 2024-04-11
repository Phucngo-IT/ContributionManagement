package spring.boot.contributionmanagement.mailService;

import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

@Component
public class MailStructure {
    private String subject = "Submit article Successfully!!!";
    private String message = "submited an article successfully, Please check and give some feedbacks to this article within 14 days!!!";

    public String getSubject() {
        return subject;
    }

    public MailStructure setSubject(String subject) {
        this.subject = subject;
        return this;
    }
    public String getMessage() {
        return message;
    }

    public MailStructure setMessage(String message) {
        this.message = message;
        return this;
    }
}
