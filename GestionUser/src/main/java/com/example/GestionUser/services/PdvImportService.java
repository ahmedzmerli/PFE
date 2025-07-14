package com.example.GestionUser.services;

import com.example.GestionUser.entities.Pdv;
import com.example.GestionUser.entities.PdvDetails;
import com.example.GestionUser.entities.PdvHistory;
import com.example.GestionUser.entities.PdvMaster;
import com.example.GestionUser.repositories.PdvDetailsRepository;
import com.example.GestionUser.repositories.PdvHistoryRepository;
import com.example.GestionUser.repositories.PdvMasterRepository;
import com.example.GestionUser.repositories.PdvRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service

public class PdvImportService {

    private final PdvMasterRepository pdvMasterRepository;
    private final PdvDetailsRepository pdvDetailsRepository;
    private final PdvRepository pdvRepository;
    private final PdvHistoryRepository pdvHistoryRepository;


    public PdvImportService(
            PdvMasterRepository pdvMasterRepository,
            PdvDetailsRepository pdvDetailsRepository,
            PdvRepository pdvRepository,
            PdvHistoryRepository pdvHistoryRepository
    ) {
        this.pdvMasterRepository = pdvMasterRepository;
        this.pdvDetailsRepository = pdvDetailsRepository;
        this.pdvRepository = pdvRepository;
        this.pdvHistoryRepository = pdvHistoryRepository;
    }

    @Transactional
    public void importExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Ignorer l'entête

                String msisdn = getCellValue(row.getCell(0));
                String nomPdv = getCellValue(row.getCell(1));
                String codePdv = getCellValue(row.getCell(2));
                String adresse = getCellValue(row.getCell(3));

                if (msisdn == null || msisdn.isEmpty()) continue;

                // ✅ Vérifier si le MSISDN existe déjà
                if (pdvRepository.existsById(msisdn)) {
                    System.out.println("MSISDN déjà existant : " + msisdn + ", ignoré.");
                    continue; // On passe à la ligne suivante
                }

                // 1. Créer le PDV master
                PdvMaster pdvMaster = new PdvMaster();
                pdvMaster.setNomPdv(nomPdv);
                pdvMaster.setAdresse(adresse);
                pdvMaster.setCodePdv(codePdv);
                pdvMasterRepository.save(pdvMaster);

                // 2. Lier le MSISDN
                PdvDetails details = new PdvDetails();
                details.setMsisdn(msisdn);
                details.setPdvMasterId(pdvMaster.getId());
                pdvDetailsRepository.save(details);

                // 3. Ajouter dans la table pdv
                Pdv pdv = new Pdv(msisdn);
                pdvRepository.save(pdv);

                // 4. Historique
                PdvHistory hist = new PdvHistory();
                hist.setPdvMasterId(pdvMaster.getId());
                hist.setActionType("IMPORT");
                hist.setUsername("import-script");
                hist.setDateAction(java.time.LocalDateTime.now());
                pdvHistoryRepository.save(hist);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'import du fichier Excel", e);
        }
    }



    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Pour éviter les ".0" en fin de String
                    double d = cell.getNumericCellValue();
                    if (d == Math.floor(d)) {
                        return String.valueOf((long) d);
                    } else {
                        return String.valueOf(d);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }


}
