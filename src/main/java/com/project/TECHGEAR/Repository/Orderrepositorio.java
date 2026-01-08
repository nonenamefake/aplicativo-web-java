package com.project.TECHGEAR.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.TECHGEAR.Model.Order;
@Repository
public interface Orderrepositorio extends JpaRepository<Order, Integer> {

    // Buscar órdenes por usuario
    List<Order> findByUserId(Integer userId);

    // Filtrar por estado
    List<Order> findByStatus(String status);

    // Obtener la última orden generada
    Order findFirstByUserIdOrderByIdDesc(Integer userId);

    // Órdenes activas o pendientes
    List<Order> findByUserIdAndStatus(Integer userId, String status);
}