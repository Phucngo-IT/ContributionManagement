package spring.boot.contributionmanagement.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "selected_contribution")
public class SelectedContribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(name = "log_download",
            joinColumns = @JoinColumn(name = "selected_contribution_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @ManyToOne
    @JoinColumn(name = "article")
    private Article article;


    public SelectedContribution() {
    }

    public Long getId() {
        return id;
    }

    public SelectedContribution setId(Long id) {
        this.id = id;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public SelectedContribution setUsers(List<User> users) {
        this.users = users;
        return this;
    }

    public Article getArticle() {
        return article;
    }

    public SelectedContribution setArticle(Article article) {
        this.article = article;
        return this;
    }

    @Override
    public String toString() {
        return "SelectedContribution{" +
                "id=" + id +
                ", users=" + users +
                ", article=" + article +
                '}';
    }
}
