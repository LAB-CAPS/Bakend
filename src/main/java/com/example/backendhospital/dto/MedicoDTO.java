package com.example.backendhospital.dto;




public class MedicoDTO {
    private Long id;
    private String dni;
    private String correo;
    private String telefono;
    private String nombres;
    private String apellidos;
    private String especialidad;
    private String codigoMedico;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getCodigoMedico() { return codigoMedico; }
    public void setCodigoMedico(String codigoMedico) { this.codigoMedico = codigoMedico; }
}
