package com.project.TECHGEAR.Controller;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import com.project.TECHGEAR.Model.User;
import com.project.TECHGEAR.Services.UsuarioService;

@Controller
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/mi-cuenta")
    public String miCuenta(Model model, org.springframework.security.core.Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User u = usuarioService.findByUsername(username);
            if (u != null) {
                model.addAttribute("usuario", u);
            }
        }
        return "Usuario/miCuenta";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("user", new User());
        return "Usuario/register";
    }

    @GetMapping("/Registrar")
    public String registrar(Model model) {
        model.addAttribute("user", new User());
        return "Usuario/register";
    }

    @PostMapping("/registrar")
    public String registrar(@Valid @ModelAttribute("user") User usuario, 
                           BindingResult result, 
                           Model model,
                           RedirectAttributes redirectAttributes,
                           @RequestParam(required = false) String passwordConfirm) {
        // Validar que la contraseña no sea nula o vacía
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            result.rejectValue("password", "error.password", "La contraseña es obligatoria");
        }


        // Validar que no haya errores de validación en los campos anotados
        if (result.hasErrors()) {
            model.addAttribute("user", usuario);
            return "Usuario/register";
        }

        // Validar unicidad de username
        if (usuarioService.existsByUsername(usuario.getUsername())) {
            result.rejectValue("username", "error.username", "El nombre de usuario ya está en uso");
            model.addAttribute("user", usuario);
            model.addAttribute("error", "El nombre de usuario ya está en uso");
            return "Usuario/register";
        }

        // Validar unicidad de email
        if (usuarioService.existsByEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.email", "El email ya está registrado");
            model.addAttribute("user", usuario);
            model.addAttribute("error", "El email ya está registrado");
            return "Usuario/register";
        }

        usuario.setUpdatedAt(Timestamp.from(Instant.now()));
        usuario.setCreatedAt(Timestamp.from(Instant.now()));
        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente. Por favor inicia sesión");
        return "redirect:/Login";
    }

    @PostMapping("/Registrarse")
    public String registarse(@Valid @ModelAttribute("user") User usuario, 
                            BindingResult result, 
                            Model model,
                            RedirectAttributes redirectAttributes) {
        // password must be provided when registering
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            result.rejectValue("password", "error.password", "La contraseña es obligatoria");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", usuario);
            return "Usuario/register";
        }
        
        // Validar unicidad de username
        if (usuarioService.existsByUsername(usuario.getUsername())) {
            result.rejectValue("username", "error.username", "El nombre de usuario ya está en uso");
            model.addAttribute("user", usuario);
            model.addAttribute("error", "El nombre de usuario ya está en uso");
            return "Usuario/register";
        }
        
        // Validar unicidad de email
        if (usuarioService.existsByEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.email", "El email ya está registrado");
            model.addAttribute("user", usuario);
            model.addAttribute("error", "El email ya está registrado");
            return "Usuario/register";
        }
        
        usuario.setUpdatedAt(Timestamp.from(Instant.now()));
        usuario.setCreatedAt(Timestamp.from(Instant.now()));
        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente");
        return "redirect:/";
    }
    
    @GetMapping("/usuario")
    public String usuario(Model model) {
        model.addAttribute("lista", usuarioService.get());
        return "Usuario/VerUsuarios";
    }

    @PostMapping("/usuario/guardar")
    public String Guardaruser(@Valid @ModelAttribute("usuario") User usuario, 
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "Usuario/nuevousuario";
        }
        
        // Validar unicidad de username
        if (usuarioService.existsByUsername(usuario.getUsername())) {
            result.rejectValue("username", "error.username", "El nombre de usuario ya está en uso");
            model.addAttribute("usuario", usuario);
            model.addAttribute("error", "El nombre de usuario ya está en uso");
            return "Usuario/nuevousuario";
        }
        
        // Validar unicidad de email
        if (usuarioService.existsByEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.email", "El email ya está registrado");
            model.addAttribute("usuario", usuario);
            model.addAttribute("error", "El email ya está registrado");
            return "Usuario/nuevousuario";
        }
        
        usuario.setUpdatedAt(Timestamp.from(Instant.now()));
        usuario.setCreatedAt(Timestamp.from(Instant.now()));
        usuarioService.save(usuario);
        return "redirect:/usuario";
    }
    
    @GetMapping("/usuario/new")
    public String Nuevouser(Model model) {
        User user = new User();
        model.addAttribute("usuario", user);
        return "Usuario/nuevousuario";
    }
    
    @GetMapping("/usuario/editar/{id}")
    public String Editaruser(@PathVariable Integer id, Model modelo) {
        modelo.addAttribute("usuario", usuarioService.get(id));
        return "Usuario/EditarUsuario";
    }

    @GetMapping("/usuario/eliminar/{id}")
    public String eliminaruser(@PathVariable Integer id) {
        usuarioService.delete(id);
        return "redirect:/usuario";
    }

    @PostMapping("/usuario/actualizar/{id}")
    public String updateuser(@PathVariable Integer id, 
                            @Valid @ModelAttribute("usuario") User usuario, 
                            BindingResult result, 
                            Model model) {
        if (result.hasErrors()) {
            usuario.setId(id);
            model.addAttribute("usuario", usuario);
            return "Usuario/EditarUsuario";
        }
        
        User now = usuarioService.get(id);
        
        // Validar unicidad de username (solo si cambió)
        if (usuario.getUsername() != null && now.getUsername() != null &&
            !now.getUsername().equals(usuario.getUsername()) && 
            usuarioService.existsByUsername(usuario.getUsername())) {
            result.rejectValue("username", "error.username", "El nombre de usuario ya está en uso");
            usuario.setId(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("error", "El nombre de usuario ya está en uso");
            return  "Usuario/EditarUsuario";
        }
        
        // Validar unicidad de email (solo si cambió)
        if (usuario.getEmail() != null && now.getEmail() != null &&
            !now.getEmail().equals(usuario.getEmail()) && 
            usuarioService.existsByEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.email", "El email ya está registrado");
            usuario.setId(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("error", "El email ya está registrado");
            return "Usuario/EditarUsuario";
        }
        
        now.setId(id);
        now.setUsername(usuario.getUsername());
        now.setFirstName(usuario.getFirstName());
        now.setEmail(usuario.getEmail());
        now.setLastName(usuario.getLastName());
        now.setUpdatedAt(Timestamp.from(Instant.now()));
        usuarioService.update(now);
        // After update, redirect appropriately
        return "redirect:/usuario";
    }

@PostMapping("/mi-cuenta/actualizar/{id}")
    public String editeruser(@PathVariable Integer id, 
                            @Valid @ModelAttribute("usuario") User usuario, 
                            Authentication authentication,
                            BindingResult result, 
                            Model model) {
        if (result.hasErrors()) {
            usuario.setId(id);
            model.addAttribute("usuario", usuario);
            return "Usuario/miCuenta";
        }
        
        User now = usuarioService.get(id);
        
        // Validar unicidad de username (solo si cambió)
        if (usuario.getUsername() != null && now.getUsername() != null &&
            !now.getUsername().equals(usuario.getUsername()) && 
            usuarioService.existsByUsername(usuario.getUsername())) {
            result.rejectValue("username", "error.username", "El nombre de usuario ya está en uso");
            usuario.setId(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("error", "El nombre de usuario ya está en uso");
            return "Usuario/miCuenta";
        }
        
        // Validar unicidad de email (solo si cambió)
        if (usuario.getEmail() != null && now.getEmail() != null &&
            !now.getEmail().equals(usuario.getEmail()) && 
            usuarioService.existsByEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.email", "El email ya está registrado");
            usuario.setId(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("error", "El email ya está registrado");
            return "Usuario/miCuenta";
        }
        
        now.setId(id);
        now.setUsername(usuario.getUsername());
        now.setFirstName(usuario.getFirstName());
        now.setEmail(usuario.getEmail());
        now.setLastName(usuario.getLastName());
        now.setUpdatedAt(Timestamp.from(Instant.now()));
        usuarioService.update(now);
        User actualizado = now;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                    System.out.println("Re-authenticating user: " + actualizado.getUsername());
                    UserDetails userDetails = usuarioService.loadUserByUsername(actualizado.getUsername());
                    UsernamePasswordAuthenticationToken newAuth =
                        new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(newAuth);
                
            }
        }
        // After update, redirect appropriately
         return "redirect:/mi-cuenta";

    }
}


