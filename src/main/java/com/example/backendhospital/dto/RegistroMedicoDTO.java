package com.example.backendhospital.dto;

import lombok.Data;

@Data
public class RegistroMedicoDTO {
    private String dni;
    private String correo;
    private String telefono;
    private String nombres;
    private String apellidos;
    private String codigoMedico;
    private Long idUsuario;
    private Long idEspecialidad;
}
