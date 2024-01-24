package com.zhelin.community.config;

import com.zhelin.community.util.CommunityConstant;
import com.zhelin.community.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user/setting", "/user/upload", "/discuss/add", "/comment/add/**", "/letter/**", "/notice/**", "/like", "/follow", "/unfollow")
                .hasAnyAuthority(AUTHORITY_USER, AUTHORITY_MODERATOR, AUTHORITY_ADMIN)
                .anyRequest().permitAll()
                .and().csrf().disable();

        // Exception handling
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    // Not login
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
                        if(request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equals("XMLHttpRequest")) {
                            response.setContentType("application/plain;charset=utf-8");
                            response.getWriter().write(CommunityUtil.getJSONString(403, "You have not logged in!"));
                        } else {
                            response.sendRedirect(request.getContextPath() + "/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandlerImpl() {
                    // Don't have permission
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
                        if(request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equals("XMLHttpRequest")) {
                            response.setContentType("application/plain;charset=utf-8");
                            response.getWriter().write(CommunityUtil.getJSONString(403, "You have not permission!"));
                        } else {
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        // logout
        http.logout().logoutUrl("/securitylogout");

    }

}
