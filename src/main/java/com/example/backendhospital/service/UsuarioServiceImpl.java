package com.example.backendhospital.service;

import com.example.backendhospital.dto.LoginResponseDTO;
import com.example.backendhospital.dto.RegistroMedicoDTO;
import com.example.backendhospital.dto.RegistroPacienteDTO;
import com.example.backendhospital.entity.*;
import com.example.backendhospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              PacienteRepository pacienteRepository,
                              MedicoRepository medicoRepository,
                              EspecialidadRepository especialidadRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.especialidadRepository = especialidadRepository;
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        usuarioRepository.findByDni(usuario.getDni()).ifPresent(u -> {
            throw new RuntimeException("Ya existe un usuario con ese DNI");
        });
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> login(String dni, String contrasena) {
        return usuarioRepository.findByDniAndContrasena(dni, contrasena);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }

    @Override
    public List<Usuario> listarUsuariosPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    @Override
    public Paciente registrarPaciente(RegistroPacienteDTO dto) {
        Usuario usuario = usuarioRepository.findByDni(dto.getDni())
            .orElseGet(() -> {
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setDni(dto.getDni());
                nuevoUsuario.setContrasena(dto.getContrasena());
                nuevoUsuario.setRol("PACIENTE");
                nuevoUsuario.setNombreUsuario(dto.getNombres() + " " + dto.getApellidos());
                nuevoUsuario.setNombres(dto.getNombres());
                nuevoUsuario.setApellidos(dto.getApellidos());
                return usuarioRepository.save(nuevoUsuario);
            });

        Paciente paciente = new Paciente();
        paciente.setUsuario(usuario);
        paciente.setDni(dto.getDni());
        paciente.setCorreo(dto.getCorreo());
        paciente.setTelefono(dto.getTelefono());
        paciente.setNombres(dto.getNombres());
        paciente.setApellidos(dto.getApellidos());
        paciente.setSeguro(Paciente.TipoSeguro.valueOf(dto.getSeguro().toUpperCase()));

        return pacienteRepository.save(paciente);
    }

@Override
public Medico registrarMedico(RegistroMedicoDTO dto) {
    Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Especialidad especialidad = especialidadRepository.findById(dto.getIdEspecialidad())
            .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

    Medico medico = new Medico();
    medico.setDni(dto.getDni());
    medico.setCorreo(dto.getCorreo());
    medico.setTelefono(dto.getTelefono());
    medico.setNombres(dto.getNombres());
    medico.setApellidos(dto.getApellidos());
    medico.setCodigoMedico(dto.getCodigoMedico());
    medico.setUsuario(usuario);
    medico.setEspecialidad(especialidad);

    return medicoRepository.save(medico);
}

    @Override
public Optional<LoginResponseDTO> loginConDatos(String dni, String contrasena) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findByDniAndContrasena(dni, contrasena);
    if (usuarioOpt.isEmpty()) {
        return Optional.empty();
    }

    Usuario usuario = usuarioOpt.get();
    LoginResponseDTO response = new LoginResponseDTO();
    response.setId((long) usuario.getId());
    response.setDni(usuario.getDni());
    response.setRol(usuario.getRol());
    response.setNombreUsuario(usuario.getNombreUsuario());

    if ("PACIENTE".equalsIgnoreCase(usuario.getRol()) && usuario.getPaciente() != null) {
        response.setIdPaciente((long) usuario.getPaciente().getId());
    } else if ("MEDICO".equalsIgnoreCase(usuario.getRol()) && usuario.getMedico() != null) {
        response.setIdMedico((long) usuario.getMedico().getId());
    }

    return Optional.of(response);
}



}
