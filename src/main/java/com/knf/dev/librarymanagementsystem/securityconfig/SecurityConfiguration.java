package com.knf.dev.librarymanagementsystem.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.knf.dev.librarymanagementsystem.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        var auth = new DaoAuthenticationProvider();

        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder);

        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
           .authorizeRequests()

                // Ai cũng được truy cập
                .antMatchers(
                    "/login",
                    "/register",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/images/**",
                    "/uploads/**"
                ).permitAll()

                // Chỉ ADMIN được quản lý sách
                .antMatchers(
                    "/add",
                    "/add-book",
                    "/update/**",
                    "/update-book/**",
                    "/remove-book/**"
                ).hasRole("ADMIN")

                // Chỉ ADMIN được quản lý tác giả
                .antMatchers(
                    "/addAuthor",
                    "/add-author",
                    "/updateAuthor/**",
                    "/update-author/**",
                    "/remove-author/**"
                ).hasRole("ADMIN")

                // Chỉ ADMIN được quản lý nhà xuất bản
                .antMatchers(
                    "/addPublisher",
                    "/add-publisher",
                    "/updatePublisher/**",
                    "/update-publisher/**",
                    "/remove-publisher/**"
                ).hasRole("ADMIN")

                // Chỉ ADMIN được quản lý thể loại
                .antMatchers(
                    "/addCategory",
                    "/add-category",
                    "/updateCategory/**",
                    "/update-category/**",
                    "/remove-category/**"
                ).hasRole("ADMIN")

                // Trang USER
                .antMatchers(
                    "/welcome"
                ).hasAnyRole("USER", "ADMIN")

                // Các trang khác chỉ cần đăng nhập
                .antMatchers(
                "/borrow/**",
                "/my-borrows",
                "/return-book/**"
            ).hasRole("USER")

            .antMatchers(
                "/favorite/**",
                "/unfavorite/**"
            ).hasRole("USER")

                .anyRequest().authenticated()

            .and()

            .formLogin()
                .loginPage("/login")

                // Sau khi đăng nhập thành công
                .successHandler(successHandler)

                .permitAll()

            .and()

            .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(
                    new AntPathRequestMatcher("/logout")
                )
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }
}