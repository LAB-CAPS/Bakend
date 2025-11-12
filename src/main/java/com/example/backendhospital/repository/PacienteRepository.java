package com.example.backendhospital.repository;

import com.example.backendhospital.entity.Medico;
import com.example.backendhospital.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByDni(String dni);
  
Optional<Paciente> findByUsuarioId(Long id);
}
