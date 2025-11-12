package com.example.backendhospital.dto;

import java.time.LocalDateTime;

public class RecetaDTO {

    private Long id;
    private Long idMedicamento; // Nuevo campo
    private String nombrePaciente;
    private String nombreMedico;
    private String medicamento;
    private String presentacion;
    private int cantidad;
    private String fechaCita;
    private String estado;
    private LocalDateTime fechaDespacho;
    // Constructor vacío (necesario para frameworks como Spring)
    public RecetaDTO() {
    }

    // Constructor con parámetros
public RecetaDTO(Long id, Long idMedicamento, String nombrePaciente, String nombreMedico,
                 String medicamento, String presentacion, int cantidad,
                 String fechaCita, String estado,LocalDateTime fechaDespacho) {
    this.id = id;
    this.idMedicamento = idMedicamento;
    this.nombrePaciente = nombrePaciente;
    this.nombreMedico = nombreMedico;
    this.medicamento = medicamento;
    this.presentacion = presentacion;
    this.cantidad = cantidad;
    this.fechaCita = fechaCita;
    this.estado = estado;
    this.fechaDespacho = fechaDespacho;
}

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

public String getEstado() {
    return estado;
}

public void setEstado(String estado) {
    this.estado = estado;
}
    
    public Long getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }


    
}
