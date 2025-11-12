package com.example.backendhospital.service;

import com.example.backendhospital.entity.Medicamento;
import com.example.backendhospital.repository.MedicamentoRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

import java.io.InputStream;

@Service
public class CargaExcelService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public void cargarMedicamentosDesdeExcel() {
    try (
        InputStream inputStream = new ClassPathResource("Medicamentos.xlsx").getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream)
    ) {
        Sheet sheet = workbook.getSheetAt(0);
        boolean primeraFila = true;

        for (Row row : sheet) {
            if (primeraFila) {
                primeraFila = false;
                continue;
            }

            if (row == null || row.getCell(1) == null) continue;

            String nombre = row.getCell(1).getStringCellValue();
            String presentacion = row.getCell(2).getStringCellValue();
            String via = row.getCell(3).getStringCellValue();

            // 🛑 Verifica si ya existe ese medicamento
            boolean existe = medicamentoRepository.existsByNombreComercialAndPresentacionAndViaAdministrativa(
                nombre, presentacion, via
            );
            if (existe) continue;

            Medicamento medicamento = new Medicamento();
            medicamento.setNombreComercial(nombre);
            medicamento.setPresentacion(presentacion);
            medicamento.setViaAdministrativa(via);

            Cell stockCell = row.getCell(4);
            int stock = (int) (stockCell.getCellType() == CellType.NUMERIC
                    ? stockCell.getNumericCellValue()
                    : Integer.parseInt(stockCell.getStringCellValue()));
            medicamento.setStock(stock);

            medicamentoRepository.save(medicamento);
        }

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error al leer archivo Excel: " + e.getMessage(), e);
    }
}


public void cargarMedicamentosDesdeArchivo(MultipartFile archivo) {
    try (InputStream inputStream = archivo.getInputStream();
         Workbook workbook = new XSSFWorkbook(inputStream)) {

        Sheet sheet = workbook.getSheetAt(0);
        boolean primeraFila = true;

        for (Row row : sheet) {
            if (primeraFila) {
                primeraFila = false;
                continue;
            }

            if (row == null || row.getCell(1) == null) continue;

            String nombre = row.getCell(1).getStringCellValue().trim();
            String presentacion = row.getCell(2).getStringCellValue().trim();
            String via = row.getCell(3).getStringCellValue().trim();

            Cell stockCell = row.getCell(4);
            int stock = (int) (stockCell.getCellType() == CellType.NUMERIC
                    ? stockCell.getNumericCellValue()
                    : Integer.parseInt(stockCell.getStringCellValue()));

            // 🔍 Verifica si ya existe
            Optional<Medicamento> existenteOpt = medicamentoRepository.findByNombreComercialIgnoreCase(nombre);

            if (existenteOpt.isPresent()) {
                // ✅ Ya existe, actualiza stock
                Medicamento existente = existenteOpt.get();
                existente.setStock(existente.getStock() + stock); // suma stock
                medicamentoRepository.save(existente);
            } else {
                // 🆕 No existe, crea nuevo
                Medicamento nuevo = new Medicamento();
                nuevo.setNombreComercial(nombre);
                nuevo.setPresentacion(presentacion);
                nuevo.setViaAdministrativa(via);
                nuevo.setStock(stock);
                medicamentoRepository.save(nuevo);
            }
        }

    } catch (Exception e) {
        throw new RuntimeException("Error al leer archivo Excel: " + e.getMessage(), e);
    }
}


}
