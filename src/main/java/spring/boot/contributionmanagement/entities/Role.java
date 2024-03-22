package spring.boot.contributionmanagement.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "role_name")
    private String roleName;


    @OneToMany(mappedBy = "role")
    private List<User> users;


    public Role() {
    }

    public Long getId() {
        return id;
    }

    public Role setId(Long id) {
        this.id = id;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public Role setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public Role setUsers(List<User> users) {
        this.users = users;
        return this;
    }

//    @Override
//    public String toString() {
//        return "Role{" +
//                "id=" + id +
//                ", roleName='" + roleName + '\'' +
//                ", users=" + users +
//                '}';
//    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }

}
