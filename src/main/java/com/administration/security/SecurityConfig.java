package com.administration.security;

import com.administration.security.filters.JwtAuthenticationFilter;
import com.administration.security.filters.JwtAuthorizationFilter;
import com.administration.service.impl.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/auth/**", // permitAll for all paths under /auth
                        "/auth/authenticate",
                        "/auth/refreshtoken",
                        "/caisse/**",
                        "/auth-controller/loginUsingPOST",
                        "/MICROADMIN/auth-controller/loginUsingPOST").permitAll()
                .antMatchers("/MICROADMIN/auth/authenticate").permitAll() // Allow all requests to this endpoint
                .antMatchers("/MICROADMIN/swagger-ui/**",
                        "/swagger-ui/**",
                        "/v2/api-docs",
                        "/MICROADMIN/v2/api-docs",
                        "/MICROADMIN/swagger-resources/**",
                        "/swagger-resources/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
        http.cors().and().csrf().disable();

    }
}
