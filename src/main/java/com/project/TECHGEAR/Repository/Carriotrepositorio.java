package com.project.TECHGEAR.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.TECHGEAR.Model.Carrito;
import java.util.List;
@Repository
public interface Carriotrepositorio extends JpaRepository<Carrito, Integer> {

    // Buscar carritos por usuario
    List<Carrito> findByUsuarioId(Integer usuarioId);

    // Buscar carritos por estado
    List<Carrito> findByEstado(String estado);

    // Buscar último carrito activo de un usuario
    Carrito findFirstByUsuarioIdAndEstadoOrderByIdDesc(Integer usuarioId, String estado);
}
