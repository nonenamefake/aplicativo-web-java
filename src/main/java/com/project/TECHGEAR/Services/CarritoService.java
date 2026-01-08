package com.project.TECHGEAR.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.TECHGEAR.Model.Carrito;
import com.project.TECHGEAR.Repository.Carriotrepositorio;

@Service
public class CarritoService {

    @Autowired
    private Carriotrepositorio carritoRepository;

    public CarritoService(Carriotrepositorio carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    // Listar todos los carritos
    public List<Carrito> listarCarritos() {
        return carritoRepository.findAll();
    }

    // Guardar o actualizar
    public Carrito guardar(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    // Buscar por ID
    public Carrito buscarPorId(Integer id) {
        return carritoRepository.findById(id).orElse(null);
    }

    // Eliminar carrito
    public void eliminar(Integer id) {
        carritoRepository.deleteById(id);
    }

    // Buscar carritos por usuario
    public List<Carrito> buscarPorUsuario(Integer usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    // Obtener carrito activo de un usuario
    public Carrito obtenerCarritoActivo(Integer usuarioId) {
        return carritoRepository.findFirstByUsuarioIdAndEstadoOrderByIdDesc(usuarioId, "Activo");
    }
}
