package com.project.TECHGEAR.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.TECHGEAR.Model.Order;
import com.project.TECHGEAR.Repository.Orderrepositorio;

@Service
public class OrderService {
    @Autowired
    private final Orderrepositorio orderRepository;

    public OrderService(Orderrepositorio orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Listar todas las órdenes
    public List<Order> listar() {
        return orderRepository.findAll();
    }

    // Guardar o actualizar una orden
    public Order guardar(Order order) {
        return orderRepository.save(order);
    }

    // Buscar por ID
    public Order buscarPorId(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Eliminar
    public void eliminar(Integer id) {
        orderRepository.deleteById(id);
    }

    // Buscar órdenes por usuario
    public List<Order> buscarPorUsuario(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    // Buscar por estado
    public List<Order> buscarPorEstado(String status) {
        return orderRepository.findByStatus(status);
    }

    // Obtener última orden creada por usuario
    public Order obtenerUltimaOrden(Integer userId) {
        return orderRepository.findFirstByUserIdOrderByIdDesc(userId);
    }

    // Obtener órdenes activas (ej: PENDIENTE)
    public List<Order> ordenesActivas(Integer userId) {
        return orderRepository.findByUserIdAndStatus(userId, "PENDIENTE");
    }
}
