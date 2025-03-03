package com.example.Minor_Project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder getEncoder(){    //PasswordEncoder is an interface which has many implementation classes ,it basically encrypts the credentials.I am returning NoOpPasswordEncoder since i dont want to store encrypted value and i want to use unencrypted credentials

        return NoOpPasswordEncoder.getInstance();   //returning NoOpPasswordEncoder means ,this doesnot  encrypt password
    }

    @Bean       //SecurityFilterChain is used for authorisation as per Authority of UserDetails
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(authorize->authorize
                        .requestMatchers("/transaction/issue").hasAuthority("STUDENT")  //we can also put like "/transaction/issue/**" which means a path followed by any
                        .requestMatchers("/transaction/return").hasAuthority("ADMIN")    //these means all these apis can be accessed only by allowed authorities
                        .requestMatchers("/book").hasAuthority("ADMIN")
                        .anyRequest().permitAll()) //we literally permitted all other methods as permitAll.We didnot bother about POSTAPI authentications also,bcoz we need CSRF token,else it wont open that POST itself
//                        .anyRequest().authenticated()) //allows only authenticated users
                .formLogin(withDefaults())  //this ensures that it can be opened in browser itself
                .httpBasic(withDefaults()) //this is used when we develop with only as backend,cause in most of the cases ,frontend will be built separately and backend separately, so it shouldnot redirect to a login page as its not required in this case and could be done only with API clients like postman.So if someone hits localhost:8080/Spring_Security in postman, he will not be redirected to login instead throws 401 error unauthorised
                .csrf(csrf-> csrf.disable());   //this is done only for testing purpose.This should be removed when our testing is done.Else other than GET methods ,for all methods it shows 403 error.This was disabled by us for testing API's which modifies resources,like POST,PUT,DELETE,PATCH API ,since these API's need csrf token for working and e cannot provide csrf token in postman each time
        return http.build();
    }

}
