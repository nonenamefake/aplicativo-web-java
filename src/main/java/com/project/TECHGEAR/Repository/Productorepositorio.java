package com.project.TECHGEAR.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.TECHGEAR.Model.Product;

@Repository
public interface Productorepositorio extends JpaRepository<Product, Integer>{
    
    // Buscar productos por nombre (búsqueda parcial, case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Alternativa usando query personalizada
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> buscarPorNombre(@Param("name") String name);

    // Buscar productos por categoría (case-insensitive exact match)
    List<Product> findByCategoryIgnoreCase(String category);

    // Buscar productos por nombre y categoría
    List<Product> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(String name, String category);
}
