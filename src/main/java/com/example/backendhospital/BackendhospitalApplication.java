package com.example.backendhospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // 👈 Agrega esto

@SpringBootApplication
@EnableScheduling // 👈 Activa las tareas programadas
public class BackendhospitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendhospitalApplication.class, args);
	}
}
