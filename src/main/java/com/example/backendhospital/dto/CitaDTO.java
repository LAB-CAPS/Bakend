package com.example.backendhospital.dto;
import com.example.backendhospital.dto.CitaDTO;

import java.time.LocalDate;


import java.time.LocalTime;


public class CitaDTO {
    private String especialidad;
    private String estado;
    private String fecha;
    private String hora;
    private Long idPaciente;
    private Long idMedico;
    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }
    public Long getIdPaciente() {
        return idPaciente;
    }
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }
    public Long getIdMedico() {
        return idMedico;
    }
    public void setIdMedico(Long idMedico) {
        this.idMedico = idMedico;
    }

    
}


