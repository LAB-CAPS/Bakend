package com.example.backendhospital.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Receta {

    @Column(name = "fecha_despacho")
private LocalDateTime fechaDespacho;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "medicamento_id")
    private Medicamento medicamento;

    private int cantidad;

    // Getters y Setters

    public LocalDateTime getFechaDespacho() {
    return fechaDespacho;
}

public void setFechaDespacho(LocalDateTime fechaDespacho) {
    this.fechaDespacho = fechaDespacho;
}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }




        @Column(nullable = false)
    private String estado = "EN_ESPERA"; // Valor por defecto

    // Getters y Setters    
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
