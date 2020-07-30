package com.elsevier.elsevierzuulgateway.config;

import com.elsevier.elsevierzuulgateway.security.JwtAuthenticationFilter;
import com.elsevier.elsevierzuulgateway.security.JwtConfig;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtConfig jwtConfig;

  public SecurityConfig(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(
            (req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and()
        .addFilterAfter(
            new JwtAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, jwtConfig.getAuthUri())
        .permitAll()
        .antMatchers("/api/greetings/admin/**")
        .hasRole("ADMIN")
        .antMatchers(HttpMethod.POST, "/api/authors/**")
        .hasRole("ADMIN")
        .anyRequest()
        .authenticated();
  }
}
