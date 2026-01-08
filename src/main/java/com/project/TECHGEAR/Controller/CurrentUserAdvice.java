package com.project.TECHGEAR.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
 
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.project.TECHGEAR.Model.User;
import com.project.TECHGEAR.Services.UsuarioService;

@ControllerAdvice
public class CurrentUserAdvice {

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute("currentUser")
    public User currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return null;
        boolean isUser = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        if (!isUser) return null;

        String username = authentication.getName();
        try {
            return usuarioService.findByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }
}
