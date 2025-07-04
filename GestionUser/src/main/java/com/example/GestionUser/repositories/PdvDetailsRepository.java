package com.example.GestionUser.repositories;

import com.example.GestionUser.entities.PdvDetails;
import com.example.GestionUser.entities.PdvDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PdvDetailsRepository extends JpaRepository<PdvDetails, PdvDetailsId> {
    List<PdvDetails> findByPdvMasterId(Long pdvMasterId);
}
