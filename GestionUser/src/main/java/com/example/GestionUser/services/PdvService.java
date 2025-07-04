package com.example.GestionUser.services;

import com.example.GestionUser.dto.PdvDTO;
import com.example.GestionUser.entities.*;
import com.example.GestionUser.repositories.PdvDetailsRepository;
import com.example.GestionUser.repositories.PdvHistoryRepository;
import com.example.GestionUser.repositories.PdvMasterRepository;
import com.example.GestionUser.repositories.PdvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PdvService {

    private final PdvRepository pdvRepository;
    private final PdvMasterRepository pdvMasterRepository;
    private final PdvDetailsRepository pdvDetailsRepository;
    private final PdvHistoryRepository pdvHistoryRepository;

    @Transactional
    public void addPdv(PdvDTO dto, String username) {
        if (pdvRepository.existsById(dto.getMsisdn())) {
            throw new IllegalStateException("Le MSISDN existe déjà");
        }

        PdvMaster pdvMaster = new PdvMaster();
        pdvMaster.setNomPdv(dto.getNomPdv());
        pdvMaster.setAdresse(dto.getAdresse());
        pdvMaster.setCodePdv(dto.getCodePdv());
        pdvMasterRepository.save(pdvMaster);

        pdvRepository.save(new Pdv(dto.getMsisdn()));

        PdvDetails details = new PdvDetails(dto.getMsisdn(), pdvMaster.getId());
        pdvDetailsRepository.save(details);

        PdvHistory history = new PdvHistory();
        history.setPdvMasterId(pdvMaster.getId());
        history.setUsername(username);
        history.setActionType("CREATE");
        history.setDateAction(LocalDateTime.now());
        pdvHistoryRepository.save(history);
    }

    @Transactional
    public void deletePdv(String msisdn, String username) {
        if (!pdvRepository.existsById(msisdn)) {
            throw new IllegalStateException("Le MSISDN n'existe pas");
        }

        List<PdvDetails> detailsList = pdvDetailsRepository.findAll().stream()
                .filter(d -> d.getMsisdn().equals(msisdn))
                .collect(Collectors.toList());

        for (PdvDetails d : detailsList) {
            pdvDetailsRepository.delete(d);

            PdvHistory history = new PdvHistory();
            history.setPdvMasterId(d.getPdvMasterId());
            history.setUsername(username);
            history.setActionType("DELETE");
            history.setDateAction(LocalDateTime.now());
            pdvHistoryRepository.save(history);
        }

        pdvRepository.deleteById(msisdn);
    }

    public List<PdvDTO> listAll() {
        return pdvDetailsRepository.findAll().stream().map(d -> {
            PdvMaster master = pdvMasterRepository.findById(d.getPdvMasterId())
                    .orElseThrow(() -> new IllegalStateException("PDV introuvable"));

            return new PdvDTO(
                    d.getMsisdn(),
                    master.getId(),
                    master.getNomPdv(),
                    master.getAdresse(),
                    master.getCodePdv()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public void addMsisdnToExistingPdv(Long pdvMasterId, String msisdn, String username) {
        PdvMaster pdvMaster = pdvMasterRepository.findById(pdvMasterId)
                .orElseThrow(() -> new IllegalStateException("PDV introuvable"));

        if (pdvRepository.existsById(msisdn)) {
            throw new IllegalStateException("MSISDN déjà existant");
        }

        pdvRepository.save(new Pdv(msisdn));

        PdvDetails details = new PdvDetails(msisdn, pdvMasterId);
        pdvDetailsRepository.save(details);

        PdvHistory history = new PdvHistory();
        history.setPdvMasterId(pdvMasterId);
        history.setUsername(username);
        history.setActionType("ADD_MSISDN");
        history.setDateAction(LocalDateTime.now());
        pdvHistoryRepository.save(history);
    }

    @Transactional
    public void removeMsisdnFromPdv(Long pdvMasterId, String msisdn, String username) {
        PdvDetailsId detailsId = new PdvDetailsId(msisdn, pdvMasterId);
        if (!pdvDetailsRepository.existsById(detailsId)) {
            throw new IllegalStateException("Le MSISDN n'est pas lié à ce PDV");
        }

        pdvDetailsRepository.deleteById(detailsId);
        pdvRepository.deleteById(msisdn);

        PdvHistory history = new PdvHistory();
        history.setPdvMasterId(pdvMasterId);
        history.setUsername(username);
        history.setActionType("DELETE_MSISDN");
        history.setDateAction(LocalDateTime.now());
        pdvHistoryRepository.save(history);
    }

    public List<String> listMsisdnsByPdv(Long pdvMasterId) {
        return pdvDetailsRepository.findByPdvMasterId(pdvMasterId).stream()
                .map(PdvDetails::getMsisdn)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePdv(Long pdvMasterId, PdvDTO dto, String username) {
        PdvMaster pdvMaster = pdvMasterRepository.findById(pdvMasterId)
                .orElseThrow(() -> new IllegalStateException("PDV introuvable"));

        pdvMaster.setNomPdv(dto.getNomPdv());
        pdvMaster.setAdresse(dto.getAdresse());
        pdvMaster.setCodePdv(dto.getCodePdv());
        pdvMasterRepository.save(pdvMaster);

        PdvHistory history = new PdvHistory();
        history.setPdvMasterId(pdvMasterId);
        history.setUsername(username);
        history.setActionType("UPDATE");
        history.setDateAction(LocalDateTime.now());
        pdvHistoryRepository.save(history);
    }

    @Transactional
    public void linkExistingMsisdnsToNewPdv(List<String> msisdns, PdvDTO dto, String username) {
        PdvMaster pdvMaster = new PdvMaster();
        pdvMaster.setNomPdv(dto.getNomPdv());
        pdvMaster.setAdresse(dto.getAdresse());
        pdvMaster.setCodePdv(dto.getCodePdv());
        pdvMasterRepository.save(pdvMaster);

        for (String msisdn : msisdns) {
            if (!pdvRepository.existsById(msisdn)) {
                throw new IllegalStateException("Le numéro " + msisdn + " n'existe pas");
            }

            PdvDetails detail = new PdvDetails(msisdn, pdvMaster.getId());
            pdvDetailsRepository.save(detail);

            PdvHistory history = new PdvHistory();
            history.setPdvMasterId(pdvMaster.getId());
            history.setUsername(username);
            history.setActionType("LINK_EXISTING_MSISDN");
            history.setDateAction(LocalDateTime.now());
            pdvHistoryRepository.save(history);
        }
    }
}
