package com.example.GestionUser.repositories.jim;

import com.example.GestionUser.entities.jim.JimDashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JimDashboardRepository extends JpaRepository<JimDashboard, String> {
    //Query
}
