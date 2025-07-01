package com.example.GestionUser.services;


import com.example.GestionUser.entities.BlHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.time.LocalDateTime;


import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BlHistoryProcedureService {
    @PersistenceContext
    private EntityManager em;

    // Adapte ce parseur pour plusieurs lignes
    public List<BlHistory> getByMsisdnUsingProcedure(String msisdn) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("prc_get_customer_BLACKLIST");
        query.registerStoredProcedureParameter("msisdn", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("ret", String.class, ParameterMode.OUT);

        query.setParameter("msisdn", msisdn);
        query.execute();

        String raw = (String) query.getOutputParameterValue("ret");
        if (raw == null || raw.trim().isEmpty()) return Collections.emptyList();

        // Ex: chaque ligne historique est séparée par ; ou \n. À adapter selon ton retour réel !
        String[] rows = raw.split(";");

        List<BlHistory> list = new ArrayList<>();
        for (String row : rows) {
            String[] arr = row.split(",");
            if (arr.length < 13) continue; // skip lignes invalides

            BlHistory bh = new BlHistory();
            bh.setMsisdn(msisdn);
            bh.setTmcode(arr[0]);
            bh.setOffre(arr[1]);
            bh.setTypeClient(arr[2]);
            bh.setSegment(arr[3]);
            bh.setStatutBl(arr[4]);
            bh.setDateDebut(parseDate(arr[5]));
            bh.setDateFin(parseDate(arr[6]));
            bh.setMotif(arr[7]);
            bh.setUsername(arr[8]);
            bh.setDateAction(parseDate(arr[9]));
            bh.setDateLastBlacklist(parseDate(arr[10]));
            bh.setTypeBlack(arr[11]);
            bh.setHourBl(arr[12]);
            list.add(bh);
        }
        return list;
    }

    private LocalDateTime parseDate(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try {
            return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return null;
        }
    }
}
