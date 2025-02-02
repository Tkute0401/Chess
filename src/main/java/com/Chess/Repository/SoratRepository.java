package com.Chess.Repository;

import com.Chess.Model.Sorat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoratRepository extends JpaRepository<Sorat, Long> {
    Sorat findSoratById(Long id);
    List<Sorat> findByStatus(String status);
    List<Sorat> findByGameStatus(Sorat.GameStatus status);
}
