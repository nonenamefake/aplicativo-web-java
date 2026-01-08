package com.project.TECHGEAR.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.TECHGEAR.Model.User;
import com.project.TECHGEAR.Repository.Usuariorepositorio;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private Usuariorepositorio usuariorepositorio;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> get(){
        return usuariorepositorio.findAll();
    }
    
    public User get(int id){
        Optional<User> user = usuariorepositorio.findById(id);
        return user.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    public void save(User usuario){
        // Codificar la contraseña con BCrypt antes de guardar
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            // Solo codificar si no está ya codificada (verificar si empieza con $2a$ o $2b$)
            if (!usuario.getPassword().startsWith("$2a$") && !usuario.getPassword().startsWith("$2b$")) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }
        usuariorepositorio.save(usuario);
    }
    
    public boolean existsByUsername(String username){
        return usuariorepositorio.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email){
        return usuariorepositorio.existsByEmail(email);
    }
    
    public User findByUsername(String username){
        return usuariorepositorio.findByUsername(username);
    }
    
    public User findByEmail(String email){
        return usuariorepositorio.findByEmail(email);
    }
    
    public void update(User usuario){
        if (usuario.getId() == null || !usuariorepositorio.existsById(usuario.getId())) {
            throw new RuntimeException("User not found with id: " + usuario.getId());
        }
        // Si se está actualizando la contraseña, codificarla con BCrypt
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            // Solo codificar si no está ya codificada (verificar si empieza con $2a$ o $2b$)
            if (!usuario.getPassword().startsWith("$2a$") && !usuario.getPassword().startsWith("$2b$")) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }
        usuariorepositorio.save(usuario);
    }
    
    public void delete(int id){
        if (!usuariorepositorio.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        usuariorepositorio.deleteById(id);
    }
        public Optional<User> findById(int id) {
        return usuariorepositorio.findById(id);
    }
    @Override
    public UserDetails loadUserByUsername(String usarname) 
                                         throws UsernameNotFoundException {
       // First try to find a registered regular user
       User user = usuariorepositorio.findByUsername(usarname);
       if (user != null) {
           var springUser = org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                   .password(user.getPassword())
                   .roles("USER") // default role for customers
                   .build();
           return springUser;
       }
       throw new UsernameNotFoundException("Usuario no encontrado: " + usarname);
    }  
}
