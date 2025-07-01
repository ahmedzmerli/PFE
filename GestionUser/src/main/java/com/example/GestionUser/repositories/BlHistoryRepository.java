package com.example.GestionUser.repositories;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GestionUser.entities.BlHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlHistoryRepository extends JpaRepository<BlHistory, Long> {
    List<BlHistory> findByMsisdn(String msisdn);
//    List<BlHistory> findByMsisdnAndDateActionBetween(String msisdn, LocalDateTime start, LocalDateTime end);
//    //@Query pour blacklist
//    //@Query pour historique


    /**
     * Hibernate traduit ce between en
     *   MySQL : WHERE date_action BETWEEN ? AND ?
     *   Oracle : WHERE DATE_ACTION BETWEEN ? AND ?
     */
    List<BlHistory> findByMsisdnAndDateActionBetween(
            String msisdn,
            LocalDateTime start,
            LocalDateTime end
    );


    @Query(
            "SELECT b " +
                    "FROM BlHistory b " +
                    "WHERE b.msisdn = :msisdn " +
                    "  AND b.dateAction >= :start " +
                    "  AND b.dateAction <  :end"
    )
    List<BlHistory> searchHistory(
            @Param("msisdn") String msisdn,
            @Param("start")  LocalDateTime start,
            @Param("end")    LocalDateTime end
    );


    Optional<BlHistory> findFirstByMsisdnOrderByDateActionDesc(String msisdn);

}
