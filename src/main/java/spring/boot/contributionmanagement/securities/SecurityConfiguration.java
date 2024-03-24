package spring.boot.contributionmanagement.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import spring.boot.contributionmanagement.services.UserService;

@Configuration
public class SecurityConfiguration {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserService service){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(service);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(
            configurer -> configurer
                    //Student
                    .requestMatchers( "/article").hasAnyRole("STUDENT","COORDINATOR", "MANAGER", "ADMIN")
                    .requestMatchers( "/article/showForm").hasRole("STUDENT")
                    .requestMatchers( "/article/save").hasRole("STUDENT")
                    .requestMatchers( "/article/update").hasRole("STUDENT")
                    .requestMatchers( "/article/delete").hasRole("STUDENT")
                    //Coordinator
                    .requestMatchers( "/comment").hasAnyRole("STUDENT", "COORDINATOR")
                    .requestMatchers( "/comment/save/").hasRole("COORDINATOR")
                    //Manager
                    .requestMatchers( "/article/manager/detail_approval").hasRole("MANAGER")
                    .requestMatchers( "/log_download/**").hasRole("MANAGER")
                    //Admin
                    .requestMatchers( "/article/admin/showDetail").hasRole("ADMIN")
                    .requestMatchers( "/academic_year/**").hasRole("ADMIN")
                    .requestMatchers( "/faculty/**").hasRole("ADMIN")
                    .requestMatchers("/register").permitAll()
                    .requestMatchers("/register/save").permitAll()
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/logout").permitAll()
//                    .requestMatchers("/home").permitAll()
                    .anyRequest().authenticated()
        ).formLogin(
                form -> form.loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .defaultSuccessUrl("/home", true)//after login successfully the system will redirect to home page
                        .permitAll()//change the login page by controller url
        ).logout(
                logout -> logout.permitAll()
        ).exceptionHandling(
                configurer -> configurer
                        .accessDeniedPage("/login/403")
                        .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/login"))// handle requiring authenticate when the user access
                        .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.NOT_FOUND), new AntPathRequestMatcher("/login/404")) // handle 404 error

        );

        return httpSecurity.build();
    }

}
