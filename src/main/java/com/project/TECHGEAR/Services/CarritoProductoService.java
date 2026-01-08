package com.project.TECHGEAR.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.TECHGEAR.Model.CarritoProducto;
import com.project.TECHGEAR.Repository.CarritoProductorepositorio;

import java.util.List;
@Service
public class CarritoProductoService {
    @Autowired
    private final CarritoProductorepositorio carritoProductoRepository;

    public CarritoProductoService(CarritoProductorepositorio carritoProductoRepository) {
        this.carritoProductoRepository = carritoProductoRepository;
    }

    // Listar todos
    public List<CarritoProducto> listar() {
        return carritoProductoRepository.findAll();
    }

    // Guardar o actualizar
    public CarritoProducto guardar(CarritoProducto carritoProducto) {
        return carritoProductoRepository.save(carritoProducto);
    }

    // Buscar por ID
    public CarritoProducto buscarPorId(Integer id) {
        return carritoProductoRepository.findById(id).orElse(null);
    }

    // Eliminar
    public void eliminar(Integer id) {
        carritoProductoRepository.deleteById(id);
    }

    // Buscar todos los productos de un carrito
    public List<CarritoProducto> buscarPorCarrito(Integer carritoId) {
        return carritoProductoRepository.findByCarritoId(carritoId);
    }

    // Buscar un producto específico dentro del carrito
    public CarritoProducto buscarProductoEnCarrito(Integer carritoId, Integer productId) {
        return carritoProductoRepository.findByCarritoIdAndProductId(carritoId, productId);
    }
}
