package com.project.TECHGEAR.Model;
import java.sql.Timestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "consultas")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    @NotNull(message = "El nombre es obligatorio")
    private String nombre;
    @Column(nullable = false)
    @NotNull(message = "El email es obligatorio")
    private String email;
    @Column(nullable = false)
    @NotNull(message = "El asunto es obligatorio")
    private String asunto;
    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull(message = "El mensaje es obligatorio")
    private String mensaje;
    @Column(nullable = false)
    private Timestamp fecha; 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
