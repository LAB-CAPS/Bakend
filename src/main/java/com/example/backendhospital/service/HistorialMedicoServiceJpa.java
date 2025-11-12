package com.example.backendhospital.service;

import com.example.backendhospital.entity.Cita;
import com.example.backendhospital.entity.HistoriaClinica;
import com.example.backendhospital.entity.Paciente;
import com.example.backendhospital.repository.CitaRepository;
import com.example.backendhospital.repository.HistoriaClinicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialMedicoServiceJpa implements HistoriaClinicaService {

    private final HistoriaClinicaRepository repository;
    private final CitaRepository citaRepository;

    @Autowired
    public HistorialMedicoServiceJpa(HistoriaClinicaRepository repository,
                                     CitaRepository citaRepository) {
        this.repository = repository;
        this.citaRepository = citaRepository;
    }

    @Override
    public HistoriaClinica guardarHistorial(HistoriaClinica historial) {
        return repository.save(historial);
    }

    @Override
    public List<HistoriaClinica> obtenerPorPaciente(Paciente paciente) {
        return repository.findByPaciente(paciente);
    }
@Override
public List<HistoriaClinica> obtenerPorPacienteId(int idPaciente) {
    return repository.findByPaciente_Id(Long.valueOf(idPaciente));
}
    @Override
    public Optional<HistoriaClinica> obtenerPorId(int id) {
        return repository.findById(id);
    }

    @Override
    public void cambiarEstado(Long id, String estado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        cita.setEstado(estado);
        citaRepository.save(cita);
    }

    @Override
public HistoriaClinica actualizarHistoria(HistoriaClinica historia) {
    return repository.save(historia);
}

}
