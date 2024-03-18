package spring.boot.contributionmanagement.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "log_download")
public class LogDownload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "download_date")
    private Date downloadDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(name = "download_histories", joinColumns = @JoinColumn(name = "log_download_id"), inverseJoinColumns = @JoinColumn(name = "article_id"))
   private List<Article> articles;


    public LogDownload() {
    }

    public Long getId() {
        return id;
    }

    public LogDownload setId(Long id) {
        this.id = id;
        return this;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public LogDownload setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
        return this;
    }

    public User getUser() {
        return user;
    }

    public LogDownload setUser(User user) {
        this.user = user;
        return this;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public LogDownload setArticles(List<Article> articles) {
        this.articles = articles;
        return this;
    }

    @Override
    public String toString() {
        return "LogDownload{" +
                "id=" + id +
                ", downloadDate=" + downloadDate +
                ", user=" + user +
                ", articles=" + articles +
                '}';
    }
}
