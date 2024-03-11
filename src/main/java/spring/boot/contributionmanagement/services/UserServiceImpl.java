package spring.boot.contributionmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.boot.contributionmanagement.entities.Role;
import spring.boot.contributionmanagement.entities.User;
import spring.boot.contributionmanagement.repositories.RoleRepository;
import spring.boot.contributionmanagement.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    //security
    @Override
    public User findByUsername(String username) {
        return this.userRepository.findUserByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid email or password.");
        }else {
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getRoleToAuthorities(user.getRole()));
        }
    }

    private Collection<? extends GrantedAuthority> getRoleToAuthorities(Role role){
        String roleName = role.getRoleName();
        if (roleName == null || roleName.isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        return Collections.singleton(authority);
    }


//
    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.findById(id).get();
    }

    @Override
    public void save(User user) {
        this.userRepository.save(user);
    }

    @Override
    public void saveAndUpdate(User user) {
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }


}
