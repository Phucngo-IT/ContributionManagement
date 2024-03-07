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
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "log_download",
            joinColumns = @JoinColumn(name = "selected_contribution_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @ManyToOne
    @JoinColumn(name = "article")
    private Article article;

}
