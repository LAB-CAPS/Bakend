package com.example.backendhospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Paciente paciente;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Medico medico;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dni")
    private String dni;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "rol")
    private String rol;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    // ✅ Nuevo método
    public String getNombreCompleto() {
        return this.nombres + " " + this.apellidos;
    }

    // Getters
    public Long getId() { return id; }
    public String getDni() { return dni; }
    public String getContrasena() { return contrasena; }
    public String getRol() { return rol; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public Paciente getPaciente() { return paciente; }
    public Medico getMedico() { return medico; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setDni(String dni) { this.dni = dni; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setRol(String rol) { this.rol = rol; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public void setMedico(Medico medico) { this.medico = medico; }
}
