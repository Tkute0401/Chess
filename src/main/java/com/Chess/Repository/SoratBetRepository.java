package com.Chess.Repository;


import com.Chess.Model.SoratBet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoratBetRepository extends JpaRepository<SoratBet, Long> {
    List<SoratBet> findBySorat_Id(Long soratId);
    List<SoratBet> findByUserId(Long userId);
    List<SoratBet> findBySorat_IdAndSettled(Long soratId, Boolean settled);
    List<SoratBet> findByUserIdAndSorat_Id(Long userId, Long soratId);
}
