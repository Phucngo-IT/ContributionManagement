package spring.boot.contributionmanagement.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Date dateComment;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.REFRESH})
    @JoinColumn(name = "article_id")
    private Article article;


    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;


    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public Comment setId(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Comment setContent(String content) {
        this.content = content;
        return this;
    }

    public Date getDateComment() {
        return dateComment;
    }

    public Comment setDateComment(Date dateComment) {
        this.dateComment = dateComment;
        return this;
    }

    public Article getArticle() {
        return article;
    }

    public Comment setArticle(Article article) {
        this.article = article;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Comment setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", dateComment=" + dateComment +
                ", article=" + article +
                ", user=" + user +
                '}';
    }
}
