package com.project.TECHGEAR.Model;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "carrito")
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "idusuario")
    private User usuario;

    @OneToMany(mappedBy = "carrito")
    private List<CarritoProducto> productos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public List<CarritoProducto> getProductos() {
        return productos;
    }

    public void setProductos(List<CarritoProducto> productos) {
        this.productos = productos;
    }

    
}
