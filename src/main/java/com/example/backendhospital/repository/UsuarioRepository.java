package com.example.backendhospital.repository;

import com.example.backendhospital.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {  // ← CAMBIO AQUÍ
    Optional<Usuario> findByDniAndContrasena(String dni, String contrasena);
    Optional<Usuario> findByDni(String dni);
    List<Usuario> findByRol(String rol);
}
