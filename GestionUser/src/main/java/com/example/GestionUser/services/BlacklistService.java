package com.example.GestionUser.services;




import com.example.GestionUser.handler.ApiException;
import com.example.GestionUser.handler.BusinessErrorCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.GestionUser.dto.BlacklistDTO;
import com.example.GestionUser.entities.BlHistory;
import com.example.GestionUser.repositories.BlHistoryRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlacklistService {
    private final BlHistoryRepository repository;

    public List<BlacklistDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

//    public void toggleBlacklist(Long id) {
//        BlHistory record = repository.findById(id).orElseThrow();
//        record.setStatutBl(record.getStatutBl().equalsIgnoreCase("BLACKLISTED") ? "WHITELISTED" : "BLACKLISTED");
//        record.setDateAction(java.time.LocalDateTime.now());
//        repository.save(record);
//    }

//    public void toggleBlacklist(Long id, String username) {
//        BlHistory previous = repository.findById(id)
//                .orElseThrow(() -> new ApiException(BusinessErrorCodes.BLACKLIST_ENTRY_NOT_FOUND));
//
//        String currentStatut = previous.getStatutBl();
//        String newStatut = (currentStatut != null && currentStatut.equalsIgnoreCase("BLACKLISTED"))
//                ? "WHITELISTED"
//                : "BLACKLISTED";
//
//        BlHistory newEntry = new BlHistory();
//        newEntry.setMsisdn(previous.getMsisdn());
//        newEntry.setUsername(username);
//        newEntry.setMotif(previous.getMotif());
//        newEntry.setStatutBl(newStatut);
//        newEntry.setOffre(previous.getOffre());
//        newEntry.setSegment(previous.getSegment());
//        newEntry.setTmcode(previous.getTmcode());
//        newEntry.setTypeClient(previous.getTypeClient());
//        newEntry.setDateDebut(LocalDateTime.now());
//        newEntry.setDateFin(LocalDateTime.now().plusDays(30));
//        newEntry.setDateAction(LocalDateTime.now());
//        newEntry.setDateLastBlacklist(previous.getDateLastBlacklist());
//        newEntry.setTypeBlack(previous.getTypeBlack());
//        newEntry.setHourBl(LocalDateTime.now().toLocalTime().toString());
//
//        repository.save(newEntry);
//    }
//
//
//
//
//    public BlacklistDTO toDTO(BlHistory b) {
//        long duree = java.time.temporal.ChronoUnit.DAYS.between(b.getDateDebut(), b.getDateFin());
//
//        BlacklistDTO dto = new BlacklistDTO();
//        dto.setId(b.getId());
//        dto.setMsisdn(b.getMsisdn());
//        dto.setSegment(b.getSegment());
//        dto.setDateDebut(b.getDateDebut());
//        dto.setDateFin(b.getDateFin());
//        dto.setMotif(b.getMotif());
//        dto.setOffre(b.getOffre());
//        dto.setStatut(b.getStatutBl());
//        dto.setUsername(b.getUsername());
//        dto.setTypeClient(b.getTypeClient());
//        dto.setDureeBlacklist(duree);
//        dto.setDateAction(b.getDateAction());
//
//        return dto;
//    }



//    public BlHistory blacklistMsisdn(String msisdn, String motif, String username, String typeClient, int duree) {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime dateDebut = now;
//        LocalDateTime dateFin = now.plusDays(duree);
//
//        BlHistory newEntry = new BlHistory();
//        newEntry.setMsisdn(msisdn);
//        newEntry.setMotif(motif);
//        newEntry.setStatutBl("BL");
//        newEntry.setUsername(username);
//        newEntry.setDateDebut(dateDebut);
//        newEntry.setDateFin(dateFin);
//        newEntry.setDateAction(now);
//        newEntry.setTypeBlack(duree + "-jours");
//        newEntry.setDateLastBlacklist(now);
//        newEntry.setHourBl(now.toLocalTime().toString());
//        newEntry.setTypeClient(typeClient);
//        newEntry.setOffre("N/A");
//        newEntry.setSegment("N/A");
//        newEntry.setTmcode("N/A");
//
//        return repository.save(newEntry);
//    }

    public BlacklistDTO toDTO(BlHistory entity) {
        BlacklistDTO dto = new BlacklistDTO();
        dto.setId(entity.getId());
        dto.setMsisdn(entity.getMsisdn());
        dto.setMotif(entity.getMotif());
        dto.setStatut(entity.getStatutBl());
        dto.setUsername(entity.getUsername());
        dto.setDateDebut(entity.getDateDebut());
        dto.setDateFin(entity.getDateFin());
        dto.setDateAction(entity.getDateAction());

        // Gestion robuste de typeBlack
        if (entity.getTypeBlack() != null) {
            try {
                String numericPart = entity.getTypeBlack().replace("-jours", "");
                dto.setDureeBlacklist(Long.parseLong(numericPart));
            } catch (NumberFormatException e) {
                // En cas d'erreur, définir une valeur par défaut ou null
                System.err.println("Format invalide pour typeBlack dans l'entité ID " + entity.getId() + ": " + entity.getTypeBlack());
                dto.setDureeBlacklist(null);
            }
        } else {
            dto.setDureeBlacklist(null);
        }

        dto.setTypeClient(entity.getTypeClient());
        dto.setOffre(entity.getOffre());
        dto.setSegment(entity.getSegment());
        return dto;
    }


//
//
//    public BlHistory whitelistMsisdn(String msisdn, String username, String typeClient) {
//        LocalDateTime now = LocalDateTime.now();
//
//        BlHistory newEntry = new BlHistory();
//        newEntry.setMsisdn(msisdn);
//        newEntry.setStatutBl("WL");
//        newEntry.setUsername(username);
//        newEntry.setDateAction(now);
//        newEntry.setHourBl(now.toLocalTime().toString());
//        newEntry.setTypeClient(typeClient);
//        newEntry.setOffre("N/A");
//        newEntry.setSegment("N/A");
//        newEntry.setTmcode("N/A");
//
//        return repository.save(newEntry);
//    }
//
//
//
//
//
//
//


    public BlHistory blacklistMsisdn(String msisdn, String motif, String username, int duree) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateDebut = now;
        LocalDateTime dateFin = now.plusDays(duree);

        // Cherche la dernière ligne d'historique pour ce numéro
        BlHistory last = repository.findFirstByMsisdnOrderByDateActionDesc(msisdn).orElse(null);

        BlHistory newEntry = new BlHistory();
        newEntry.setMsisdn(msisdn);
        newEntry.setMotif(motif);
        newEntry.setStatutBl("BL");
        newEntry.setUsername(username);
        newEntry.setDateDebut(dateDebut);
        newEntry.setDateFin(dateFin);
        newEntry.setDateAction(now);
        newEntry.setTypeBlack(duree + "-jours");
        newEntry.setDateLastBlacklist(now);
        newEntry.setHourBl(now.toLocalTime().toString());

        // Copie les autres champs inchangés
        if (last != null) {
            newEntry.setOffre(last.getOffre());
            newEntry.setSegment(last.getSegment());
            newEntry.setTypeClient(last.getTypeClient());
            newEntry.setTmcode(last.getTmcode());
        }

        return repository.save(newEntry);
    }




    public BlHistory whitelistMsisdn(String msisdn, String username) {
        LocalDateTime now = LocalDateTime.now();

        // Récupère la dernière entrée
        BlHistory last = repository.findFirstByMsisdnOrderByDateActionDesc(msisdn).orElse(null);

        if (last == null) {
            // Optionnel : gérer le cas où aucun historique n’existe
            throw new IllegalArgumentException("Aucun historique pour ce MSISDN");
        }

        BlHistory newEntry = new BlHistory();
        newEntry.setMsisdn(msisdn);
        newEntry.setStatutBl("WL");
        newEntry.setUsername(username);
        newEntry.setDateAction(now);
        newEntry.setHourBl(now.toLocalTime().toString());

        // Copie les autres champs inchangés
        newEntry.setOffre(last.getOffre());
        newEntry.setSegment(last.getSegment());
        newEntry.setTypeClient(last.getTypeClient());
        newEntry.setTmcode(last.getTmcode());
        newEntry.setDateDebut(last.getDateDebut());
        newEntry.setDateFin(last.getDateFin());
        newEntry.setTypeBlack(last.getTypeBlack());
        newEntry.setMotif(last.getMotif());
        newEntry.setDateLastBlacklist(last.getDateLastBlacklist());

        return repository.save(newEntry);
    }


}
