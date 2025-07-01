package com.example.GestionUser.services;

import com.example.GestionUser.dto.PdvDTO;
import com.example.GestionUser.entities.Pdv;
import com.example.GestionUser.entities.PdvHistory;
import com.example.GestionUser.entities.PdvMaster;
import com.example.GestionUser.repositories.PdvHistoryRepository;
import com.example.GestionUser.repositories.PdvMasterRepository;
import com.example.GestionUser.repositories.PdvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdvService {

    private final PdvRepository pdvRepository;
    private final PdvMasterRepository pdvMasterRepository;
    private final PdvHistoryRepository pdvHistoryRepository;

    public void addPdv(PdvDTO dto, String username) {
        // Si le msisdn existe déjà dans l'ancienne table, on ne fait rien
        if (pdvRepository.existsById(dto.getMsisdn())) {
            throw new IllegalStateException("PDV déjà existant");
        }

        // Ajouter dans l'ancienne table
        pdvRepository.save(new Pdv(dto.getMsisdn()));

        // Ajouter dans la nouvelle table
        PdvMaster pdvMaster = new PdvMaster();
        pdvMaster.setMsisdn(dto.getMsisdn());
        pdvMaster.setNomPdv(dto.getNomPdv());
        pdvMaster.setAdresse(dto.getAdresse());
        pdvMaster.setCodePdv(dto.getCodePdv());
        pdvMasterRepository.save(pdvMaster);

        // Sauvegarder dans l’historique
        PdvHistory pdvHistory = new PdvHistory();
        pdvHistory.setMsisdn(dto.getMsisdn());
        pdvHistory.setNomPdv(dto.getNomPdv());
        pdvHistory.setAdresse(dto.getAdresse());
        pdvHistory.setCodePdv(dto.getCodePdv());
        pdvHistory.setUsername(username);
        pdvHistory.setActionType("CREATE");
        pdvHistory.setDateAction(LocalDateTime.now());
        pdvHistoryRepository.save(pdvHistory);
    }


    public void deletePdv(String msisdn, String username) {
        // Supprimer de pdv_master
        pdvMasterRepository.deleteById(msisdn);

        // Supprimer de la table legacy
        pdvRepository.deleteById(msisdn);

        // Historiser
        PdvHistory pdvHistory = new PdvHistory();
        pdvHistory.setMsisdn(msisdn);
        pdvHistory.setUsername(username);
        pdvHistory.setActionType("DELETE");
        pdvHistory.setDateAction(LocalDateTime.now());

        pdvHistoryRepository.save(pdvHistory);
    }


    public List<PdvDTO> listAll() {
        return pdvMasterRepository.findAll().stream().map(p -> {
            PdvDTO dto = new PdvDTO();
            dto.setMsisdn(p.getMsisdn());
            dto.setNomPdv(p.getNomPdv());
            dto.setAdresse(p.getAdresse());
            dto.setCodePdv(p.getCodePdv());
            return dto;
        }).collect(Collectors.toList());
    }

}
