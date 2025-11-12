package com.example.backendhospital.service;
import com.example.backendhospital.dto.CitaResponseDTO;


import com.example.backendhospital.entity.Cita;
import java.util.List;

public interface CitaService {
    Cita reservarCita(Cita cita);
    List<Cita> citasPorPaciente(int idPaciente);
    List<CitaResponseDTO> obtenerCitasPorMedico(Long idMedico);
    List<CitaResponseDTO> citasPorMedicoDTO(Long idMedico);
    void actualizarEstado(Long id, String estado);
}
