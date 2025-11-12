package com.example.backendhospital.repository;
import com.example.backendhospital.entity.Cita;
import com.example.backendhospital.entity.Medico;
import com.example.backendhospital.entity.Paciente;
import com.example.backendhospital.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long>
 {
   List<Cita> findByMedicoAndFecha(Medico medico, LocalDate fecha);
    List<Cita> findByPaciente(Paciente paciente);
    List<Cita> findByEspecialidad(String especialidad);
    List<Cita> findByMedico(Medico medico);
    List<Cita> findByPacienteId(Long id);
    List<Cita> findByMedicoId(Long id);
List<Cita> findByMedicoIdAndFecha(Long idMedico, LocalDate fecha);
boolean existsByMedicoAndFechaAndHora(Medico medico, LocalDate fecha, LocalTime hora);

List<Cita> findByPacienteUsuarioId(Long idUsuario);

}
