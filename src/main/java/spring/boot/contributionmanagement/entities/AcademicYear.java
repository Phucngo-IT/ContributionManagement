package spring.boot.contributionmanagement.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "academic_year")
public class AcademicYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "discription")
    private String discription;

    @Column(name = "closure_date")
    private Date closureDate;

    @Column(name = "final_closure_date")
    private Date finalClosureDate;

    @OneToMany(mappedBy = "academicYear")
    private List<Article> articles;

    //
    public AcademicYear() {
    }

    public Long getId() {
        return id;
    }

    public AcademicYear setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AcademicYear setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDiscription() {
        return discription;
    }

    public AcademicYear setDiscription(String discription) {
        this.discription = discription;
        return this;
    }

    public Date getClosureDate() {
        return closureDate;
    }

    public AcademicYear setClosureDate(Date closureDate) {
        this.closureDate = closureDate;
        return this;
    }

    public Date getFinalClosureDate() {
        return finalClosureDate;
    }

    public AcademicYear setFinalClosureDate(Date finalClosureDate) {
        this.finalClosureDate = finalClosureDate;
        return this;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public AcademicYear setArticles(List<Article> articles) {
        this.articles = articles;
        return this;
    }

    @Override
    public String toString() {
        return "AcademicYear{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", discription='" + discription + '\'' +
                ", closureDate=" + closureDate +
                ", finalClosureDate=" + finalClosureDate +
                ", articles=" + articles +
                '}';
    }
}
