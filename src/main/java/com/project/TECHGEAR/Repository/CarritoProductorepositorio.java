package com.project.TECHGEAR.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.TECHGEAR.Model.CarritoProducto;
@Repository
public interface CarritoProductorepositorio extends JpaRepository<CarritoProducto, Integer> {

    // Buscar productos dentro de un carrito
    List<CarritoProducto> findByCarritoId(Integer carritoId);

    // Buscar producto específico dentro de un carrito
    CarritoProducto findByCarritoIdAndProductId(Integer carritoId, Integer productId);
    
}
