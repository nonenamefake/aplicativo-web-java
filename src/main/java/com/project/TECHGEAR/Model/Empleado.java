package com.project.TECHGEAR.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "empleado")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idempleado;
    @Column(nullable = false)
    @NotNull(message = "El nombre del empleado es obligatorio")
    private String nombres;
    @Column(nullable = false)
    @NotNull(message = "El usuario es obligatorio")
    private String usuario;
    @Column(nullable = false)
    @NotNull(message = "La contraseña es obligatoria")
    private String password;
    @Column(nullable = false)
    @NotNull(message = "El rol es obligatorio")
    private String rol;

    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public int getIdempleado() {
        return idempleado;
    }
    public void setIdempleado(int idempleado) {
        this.idempleado = idempleado;
    }
    
}
