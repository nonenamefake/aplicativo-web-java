package com.project.TECHGEAR.Model;
import jakarta.persistence.*;

@Entity
@Table(name = "Carrito_producto")
public class CarritoProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarritoProducto;

    private Double cantidad;

    @ManyToOne
    @JoinColumn(name = "products_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "Carrito_id")
    private Carrito carrito;

    public Integer getIdCarritoProducto() {
        return idCarritoProducto;
    }

    public void setIdCarritoProducto(Integer idCarritoProducto) {
        this.idCarritoProducto = idCarritoProducto;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }
    
}
