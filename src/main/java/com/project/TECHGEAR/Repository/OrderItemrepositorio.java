package com.project.TECHGEAR.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.TECHGEAR.Model.OrderItem;
@Repository
public interface OrderItemrepositorio extends JpaRepository<OrderItem, Integer> {

    // Obtener todos los items de una orden
    List<OrderItem> findByOrderId(Integer orderId);

    // Verificar si un producto ya pertenece a una orden
    OrderItem findByOrderIdAndProductId(Integer orderId, Integer productId);
    
}
