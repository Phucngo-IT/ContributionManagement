package spring.boot.contributionmanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //show image
    @Transient
    public String getAvatarPath(){
        if (avatar == null || id == null) return null;
        return "/src/main/resources/static/userAvatar/" + id +"/" +avatar;
        //<img th:src="@{ ${user.avatarPath} }" alt="" width="500px" height="250px">
    }

    @Column(name = "full_name")
    @NotBlank(message = "You cannot leave this section blank")
    private String fullName;

    @Column(name = "username")
    @NotBlank(message = "You cannot leave this section blank")
    private String username;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;


    @Column(name = "address")
    @NotBlank(message = "You cannot leave this section blank")
    private String address;

    @Column(name = "email")
    @NotBlank(message = "You cannot leave this section blank")
    private String email;

    @Column(name = "password")
    @Size(min = 8, message = "Minimum length is eight characters")
    private String password;

    @Column(name = "avatar")
    private String avatar;

    //role

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "role_id")
    private Role role;

    //faculty
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;


    //article
    @OneToMany(mappedBy = "user")
    private List<Article> articles;

    //comment
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;


    @OneToMany(mappedBy = "user")
    private List<LogDownload> logDownloads;
    //


    public User() {
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }



    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public User setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public User setFaculty(Faculty faculty) {
        this.faculty = faculty;
        return this;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public User setArticles(List<Article> articles) {
        this.articles = articles;
        return this;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public User setComments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public List<LogDownload> getLogDownloads() {
        return logDownloads;
    }

    public User setLogDownloads(List<LogDownload> logDownloads) {
        this.logDownloads = logDownloads;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", role=" + role +
                ", faculty=" + faculty +
                ", articles=" + articles +
                ", comments=" + comments +
                ", logDownloads=" + logDownloads +
                '}';
    }
}
