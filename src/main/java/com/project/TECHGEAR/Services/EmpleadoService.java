package com.project.TECHGEAR.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.project.TECHGEAR.Model.Empleado;
import com.project.TECHGEAR.Repository.Empleadorepositorio;

@Service
public class EmpleadoService implements UserDetailsService{
    @Autowired
    private Empleadorepositorio empleadorepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<Empleado> get(){
        return empleadorepositorio.findAll();
    }
    public Empleado get(int id){
        return empleadorepositorio.findById(id).orElseThrow(() -> new RuntimeException("Empleado not found with id: " + id));
    }
    public void save(Empleado empleado){
        // Encode employee password before saving (avoid double-encoding)
        if (empleado.getPassword() != null && !empleado.getPassword().isEmpty()) {
            if (!empleado.getPassword().startsWith("$2a$") && !empleado.getPassword().startsWith("$2b$")) {
                empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));
            }
        }
        empleadorepositorio.save(empleado);
    }
    public void update(Empleado empleado){
        if (!empleadorepositorio.existsById(empleado.getIdempleado())) {
            throw new RuntimeException("Empleado not found with id: " + empleado.getIdempleado());
        }
        // Encode password if it was changed
        if (empleado.getPassword() != null && !empleado.getPassword().isEmpty()) {
            if (!empleado.getPassword().startsWith("$2a$") && !empleado.getPassword().startsWith("$2b$")) {
                empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));
            }
        }
        empleadorepositorio.save(empleado);
    }
    public void delete(int id){
        if (!empleadorepositorio.existsById(id)) {
            throw new RuntimeException("Empleado not found with id: " + id);
        }
        empleadorepositorio.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Empleado emp = empleadorepositorio.findByUsuario(username);
        if (emp == null) {
            throw new UsernameNotFoundException("Empleado no encontrado: " + username);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String rawRol = emp.getRol() != null ? emp.getRol().toLowerCase() : "empleado";
        if (rawRol.contains("admin") || rawRol.contains("administrador")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            // any other role string becomes EMPLOYEE
            authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        }

        return org.springframework.security.core.userdetails.User
            .withUsername(emp.getUsuario())
            .password(emp.getPassword())
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }

}