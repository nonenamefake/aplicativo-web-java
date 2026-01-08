package com.project.TECHGEAR.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.TECHGEAR.Model.Product;
import com.project.TECHGEAR.Repository.Productorepositorio;


@Service
public class ProductService {
    @Autowired
    private Productorepositorio productorepositorio;


    public List<Product> get(){
        return productorepositorio.findAll();
    }
    public Product get(int id){
        return productorepositorio.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    public void save(Product product){
        productorepositorio.save(product);
    }
    public void update(Product product){
        if (product.getId() == null || !productorepositorio.existsById(product.getId())) {
            throw new RuntimeException("Product not found with id: " + product.getId());
        }
        productorepositorio.save(product);
    }
    public void delete(int id){
        if (!productorepositorio.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productorepositorio.deleteById(id);
    }
    
    // Buscar productos por nombre
    public List<Product> buscarPorNombre(String nombre){
        if (nombre == null || nombre.trim().isEmpty()) {
            return get(); // Si no hay término de búsqueda, retornar todos
        }
        return productorepositorio.findByNameContainingIgnoreCase(nombre.trim());
    }

    // Buscar productos por categoría (filtrado exacto case-insensitive)
    public List<Product> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return get();
        }
        return productorepositorio.findByCategoryIgnoreCase(categoria.trim());
    }

    // Buscar productos por nombre y categoría combinados
    public List<Product> buscarPorNombreYCategoria(String nombre, String categoria) {
        boolean hasNombre = nombre != null && !nombre.trim().isEmpty();
        boolean hasCategoria = categoria != null && !categoria.trim().isEmpty();
        if (!hasNombre && !hasCategoria) {
            return get();
        }
        if (hasNombre && hasCategoria) {
            return productorepositorio.findByNameContainingIgnoreCaseAndCategoryIgnoreCase(nombre.trim(), categoria.trim());
        }
        if (hasNombre) return buscarPorNombre(nombre);
        return buscarPorCategoria(categoria);
    }
}
