package com.project.TECHGEAR.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.TECHGEAR.Model.Empleado;

@Repository
public interface Empleadorepositorio extends JpaRepository<Empleado, Integer> {
	Empleado findByUsuario(String usuario);
	boolean existsByUsuario(String usuario);
    
}
