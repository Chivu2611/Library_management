package com.knf.dev.librarymanagementsystem.securityconfig;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        for (GrantedAuthority authority : authentication.getAuthorities()) {

            // Nếu là ADMIN
            if (authority.getAuthority().equals("ROLE_ADMIN")) {

                response.sendRedirect("/dashboard");
                return;
            }

            // Nếu là USER
            if (authority.getAuthority().equals("ROLE_USER")) {

                response.sendRedirect("/welcome");
                return;
            }
        }

        // Trường hợp tài khoản không có role phù hợp
        response.sendRedirect("/login?error");
    }
}