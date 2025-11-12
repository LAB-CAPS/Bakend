package com.example.backendhospital.dto;

public class RegistroPacienteDTO {
    private String dni;
    private String contrasena;
    private String correo;
    private String telefono;
    private String nombres;
    private String apellidos;
    private String seguro; // "SIS" o "ESSALUD"

    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getSeguro() {
        return seguro;
    }
    public void setSeguro(String seguro) {
        this.seguro = seguro;
    }

    
}
