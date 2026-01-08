package com.project.TECHGEAR.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.project.TECHGEAR.Services.EmpleadoService;

import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class Segurityemp {
        @Bean
    @Order(1)
    public SecurityFilterChain empleadoSecurityFilterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler, DaoAuthenticationProvider empleadoAuthProvider) 
    throws Exception {
        http.securityMatcher( "/usuario/**","/empleado/**", "/producto/**","/Consultas/**", "/loginemp", "/loginempend","/logoutemp");
        http.securityContext(sc -> sc.securityContextRepository(employeeSecurityContextRepository()));
        return http
            // register employee authentication provider
            .authenticationProvider(empleadoAuthProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/JavaScript/**", "/images/**", "/static/**", "/webfonts/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/loginemp")
                .loginProcessingUrl("/loginempend")
                .defaultSuccessUrl("/empleado")
                .usernameParameter("usuario")
                .successHandler(successHandler)
                .permitAll()
            )
            .logout(config -> config
                .logoutUrl("/logoutemp")
                // don't invalidate session so other security contexts are preserved
                .invalidateHttpSession(false)
                .addLogoutHandler(removeEmployeeSecurityContext())
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .build();
    }

    @Bean
    public DaoAuthenticationProvider empleadoAuthProvider(EmpleadoService empleadoDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(empleadoDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public HttpSessionSecurityContextRepository employeeSecurityContextRepository() {
        HttpSessionSecurityContextRepository r = new HttpSessionSecurityContextRepository();
        r.setSpringSecurityContextKey("EMPLOYEE_SECURITY_CONTEXT");
        return r;
    }

    private LogoutHandler removeEmployeeSecurityContext() {
        return (request, response, authentication) -> {
            var session = request.getSession(false);
            if (session != null) {
                session.removeAttribute("EMPLOYEE_SECURITY_CONTEXT");
            }
        };
    }
}
