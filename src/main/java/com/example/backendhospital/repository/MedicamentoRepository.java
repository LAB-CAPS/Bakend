package com.example.backendhospital.repository;
import java.util.Optional;

import com.example.backendhospital.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    boolean existsByNombreComercialAndPresentacionAndViaAdministrativa(String nombreComercial, String presentacion, String viaAdministrativa);
    Optional<Medicamento> findByNombreComercialIgnoreCase(String nombreComercial);

}
