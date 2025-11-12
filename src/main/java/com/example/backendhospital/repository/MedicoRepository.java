package com.example.backendhospital.repository;

import com.example.backendhospital.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    // ✅ Buscar por el nombre de la especialidad (campo especialidad.nombre)
    List<Medico> findByEspecialidad_Nombre(String nombre);

    // ✅ Buscar médico por ID del usuario (para asociación @OneToOne con Usuario)
    Optional<Medico> findByUsuarioId(Long id);
}
