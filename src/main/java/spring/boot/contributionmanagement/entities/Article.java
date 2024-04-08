package spring.boot.contributionmanagement.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //get image
    @Transient
    public String getArticleImagePath(){
        if (imageArticle == null || id == null ) return null;
        return "/src/main/resources/static/articleImage/" + id +"/" +imageArticle;
    }

    @Column(name = "title")
    private String title;

    @Column(name = "discription")
    private String discription;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "image_article")
    private String imageArticle;

    @Column(name = "status")
    private Status status  ;

    @Column(name = "upload_date")
    private Date uploadDate;
//
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "academic_year_id")
    private AcademicYear academicYear;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(name = "download_histories", joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "log_download_id"))
    private List<LogDownload> logDownloads;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<Comment> comments;



    public Article() {
    }

    public enum Status{
        approved,
        rejeck,

        pending,
        publish
    }

    public Long getId() {
        return id;
    }

    public Article setId(Long id) {
        this.id = id;
        return this;
    }

    public List<LogDownload> getLogDownloads() {
        return logDownloads;
    }

    public Article setLogDownloads(List<LogDownload> logDownloads) {
        this.logDownloads = logDownloads;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Article setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDiscription() {
        return discription;
    }

    public Article setDiscription(String discription) {
        this.discription = discription;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public Article setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getImageArticle() {
        return imageArticle;
    }

    public Article setImageArticle(String imageArticle) {
        this.imageArticle = imageArticle;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public Article setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Article setUser(User user) {
        this.user = user;
        return this;
    }

    public AcademicYear getAcademicYear() {
        return academicYear;
    }

    public Article setAcademicYear(AcademicYear academicYear) {
        this.academicYear = academicYear;
        return this;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Article setComments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", discription='" + discription + '\'' +
                ", fileName='" + fileName + '\'' +
                ", imageArticle='" + imageArticle + '\'' +
                ", status=" + status +
                ", uploadDate=" + uploadDate +
                ", user=" + user +
                ", academicYear=" + academicYear +
                ", logDownloads=" + logDownloads +
                ", comments=" + comments +
                '}';
    }
}
