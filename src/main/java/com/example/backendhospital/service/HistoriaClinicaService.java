package com.example.backendhospital.service;

import com.example.backendhospital.entity.HistoriaClinica;
import com.example.backendhospital.entity.Paciente;

import java.util.List;
import java.util.Optional;

public interface HistoriaClinicaService {

    HistoriaClinica guardarHistorial(HistoriaClinica historial);

    List<HistoriaClinica> obtenerPorPaciente(Paciente paciente); 

    List<HistoriaClinica> obtenerPorPacienteId(int idPaciente);

    Optional<HistoriaClinica> obtenerPorId(int id);
    void cambiarEstado(Long id, String estado);

    HistoriaClinica actualizarHistoria(HistoriaClinica historia);
}
