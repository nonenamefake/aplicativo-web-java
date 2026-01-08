package com.project.TECHGEAR.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.TECHGEAR.Model.OrderItem;
import com.project.TECHGEAR.Repository.OrderItemrepositorio;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private final OrderItemrepositorio orderItemRepository;

    public OrderItemService(OrderItemrepositorio orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    // Listar todos
    public List<OrderItem> listar() {
        return orderItemRepository.findAll();
    }

    // Guardar o actualizar
    public OrderItem guardar(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    // Buscar por ID
    public OrderItem buscarPorId(Integer id) {
        return orderItemRepository.findById(id).orElse(null);
    }

    // Eliminar
    public void eliminar(Integer id) {
        orderItemRepository.deleteById(id);
    }

    // Obtener items de una orden
    public List<OrderItem> buscarPorOrden(Integer orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    // Buscar un item específico dentro de la orden
    public OrderItem buscarPorOrdenYProducto(Integer orderId, Integer productId) {
        return orderItemRepository.findByOrderIdAndProductId(orderId, productId);
    }
}
