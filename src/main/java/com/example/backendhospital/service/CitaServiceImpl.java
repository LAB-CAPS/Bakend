package com.example.backendhospital.service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backendhospital.dto.CitaResponseDTO;
import com.example.backendhospital.entity.Cita;
import com.example.backendhospital.entity.Medico;
import com.example.backendhospital.entity.Paciente;
import com.example.backendhospital.repository.CitaRepository;
import com.example.backendhospital.repository.MedicoRepository;
import com.example.backendhospital.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public Cita reservarCita(Cita cita) {
        return citaRepository.save(cita);
    }

    @Override
    public List<Cita> citasPorPaciente(int idPaciente) {
        return pacienteRepository.findById((long) idPaciente)
                .map(citaRepository::findByPaciente)
                .orElse(List.of());
    }

    @Override
    public List<CitaResponseDTO> citasPorMedicoDTO(Long idMedico) {
        return obtenerCitasPorMedico(idMedico); // puedes cambiar esto si necesitas lógica diferente
    }
@Override
public List<CitaResponseDTO> obtenerCitasPorMedico(Long idMedico) {
    Medico medico = medicoRepository.findById(idMedico)
        .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

    List<Cita> citas = citaRepository.findByMedico(medico);

    return citas.stream().map(cita -> {
        CitaResponseDTO dto = new CitaResponseDTO();
        dto.setId((long) cita.getId());
        dto.setFecha(cita.getFecha().toString());
        dto.setHora(cita.getHora().toString());
        dto.setEspecialidad(cita.getEspecialidad());
        dto.setEstado(cita.getEstado());

        Paciente paciente = cita.getPaciente();
        if (paciente != null) {
            dto.setNombrePaciente(paciente.getNombres() + " " + paciente.getApellidos());
            dto.setDniPaciente(paciente.getDni()); // ✅ AÑADIDO AQUÍ
        } else {
            dto.setNombrePaciente("N/D");
            dto.setDniPaciente("N/D"); // Opcional
        }

        dto.setNombreMedico(medico.getNombres() + " " + medico.getApellidos());

        return dto;
    }).toList();
}


   // import java.util.Optional;

@Transactional
public void actualizarEstado(Long id, String estado) {
    Cita cita = citaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
    cita.setEstado(estado);
    citaRepository.save(cita);
}


}
