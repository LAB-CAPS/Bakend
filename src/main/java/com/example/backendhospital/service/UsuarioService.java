package com.example.backendhospital.service;
import com.example.backendhospital.dto.LoginDTO;
import com.example.backendhospital.dto.LoginResponseDTO;
import com.example.backendhospital.dto.RegistroPacienteDTO;
import com.example.backendhospital.dto.RegistroMedicoDTO;
import com.example.backendhospital.entity.Usuario;
import com.example.backendhospital.entity.Paciente;
import com.example.backendhospital.entity.Medico;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UsuarioService {
    Usuario registrarUsuario(Usuario usuario);
    Optional<Usuario> login(String dni, String contrasena);
    Optional<Usuario> obtenerUsuarioPorDni(String dni);
    List<Usuario> listarUsuariosPorRol(String rol);
Optional<LoginResponseDTO> loginConDatos(String dni, String contrasena);


    Paciente registrarPaciente(RegistroPacienteDTO dto);
  Medico registrarMedico(RegistroMedicoDTO dto);
}
