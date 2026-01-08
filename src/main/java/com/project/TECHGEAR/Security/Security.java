package com.project.TECHGEAR.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
@Configuration
@EnableWebSecurity
public class Security {
    @Bean
    public DaoAuthenticationProvider usuarioAuthProvider(com.project.TECHGEAR.Services.UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler, DaoAuthenticationProvider usuarioAuthProvider)
                                                   throws Exception{
    http.securityContext(sc -> sc.securityContextRepository(userSecurityContextRepository()));

    return http.authenticationProvider(usuarioAuthProvider).authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/JavaScript/**", "/static/**", "/images/**", "/webfonts/**").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/Contacto/**").permitAll()
                .requestMatchers("/Verproducto/**").permitAll()
                .requestMatchers("/Find/**").permitAll()
                .requestMatchers("/registro").permitAll()
                .requestMatchers("/Registrar/**").permitAll()
                .requestMatchers("/registrar/**").permitAll()
                .requestMatchers("/Registrarse").permitAll()
                .requestMatchers("/Login").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/logout").permitAll()
                .requestMatchers("/TerminosCondiciones").permitAll()
                .requestMatchers("/politicas").permitAll()
                .anyRequest().authenticated()
           )
            .formLogin(form -> form
                .loginPage("/Login")
                .defaultSuccessUrl("/")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .permitAll()
           )
            .logout(config -> config
                        .logoutUrl("/logout")
                        .invalidateHttpSession(false)
                        .addLogoutHandler(removeUserSecurityContext())
                    .logoutSuccessUrl("/")
                    .permitAll()
                     )
              .build();             
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return (request, response, authentication) -> {
            var authorities = authentication.getAuthorities();
            boolean isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isEmployee = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));
            if (isAdmin || isEmployee) {
                response.sendRedirect(request.getContextPath() + "/empleado");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/");
        };
    }

    @Bean
    public HttpSessionSecurityContextRepository userSecurityContextRepository() {
        HttpSessionSecurityContextRepository r = new HttpSessionSecurityContextRepository();
        r.setSpringSecurityContextKey("USER_SECURITY_CONTEXT");
        return r;
    }

    private LogoutHandler removeUserSecurityContext() {
        return (request, response, authentication) -> {
            var session = request.getSession(false);
            if (session != null) {
                session.removeAttribute("USER_SECURITY_CONTEXT");
            }
        };
    }


}//fin de la clase

