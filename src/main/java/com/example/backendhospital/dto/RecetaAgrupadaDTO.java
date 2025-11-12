package com.example.backendhospital.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RecetaAgrupadaDTO {
    private Long idCita; // <-- CAMPO NUEVO
    private List<Long> idRecetas;
    private String nombrePaciente;
    private String nombreMedico;
    private String fechaCita;
    private String estado;
    private List<MedicamentoDTO> medicamentos;
    private String especialidad;
    private LocalDateTime fechaDespacho;

    public LocalDateTime getFechaDespacho() {
    return fechaDespacho;
}

public void setFechaDespacho(LocalDateTime fechaDespacho) {
    this.fechaDespacho = fechaDespacho;
}

    public String getEspecialidad() {
    return especialidad;
}

public void setEspecialidad(String especialidad) {
    this.especialidad = especialidad;
}

    // Getters y Setters
    public List<Long> getIdRecetas() {
    return idRecetas;
}

public void setIdRecetas(List<Long> idRecetas) {
    this.idRecetas = idRecetas;
}


    public Long getIdCita() {
        return idCita;
    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
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

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<MedicamentoDTO> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<MedicamentoDTO> medicamentos) {
        this.medicamentos = medicamentos;
    }

    // Clase interna MedicamentoDTO
    public static class MedicamentoDTO {
        private String nombre;
        private String presentacion;
        private int cantidad;

        // Getters y Setters
        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
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
    }
}
