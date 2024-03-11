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

    @Column(name = "title")
    private String title;

    @Column(name = "discription")
    private String discription;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "image_article")
    private String imageArticle;

    @Column(name = "status")
    private boolean status;

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

    @OneToMany(mappedBy = "article")
    private List<Comment> comments;

    @OneToMany(mappedBy = "article")
    private List<SelectedContribution> selectedContributions;


    public Article() {
    }

    public Long getId() {
        return id;
    }

    public Article setId(Long id) {
        this.id = id;
        return this;
    }

    public List<SelectedContribution> getSelectedContributions() {
        return selectedContributions;
    }

    public Article setSelectedContributions(List<SelectedContribution> selectedContributions) {
        this.selectedContributions = selectedContributions;
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

    public boolean isStatus() {
        return status;
    }

    public Article setStatus(boolean status) {
        this.status = status;
        return this;
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
                '}';
    }
}
