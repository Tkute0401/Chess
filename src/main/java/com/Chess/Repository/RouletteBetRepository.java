package com.Chess.Repository;


import com.Chess.Model.RouletteBet;
import com.Chess.Model.SoratBet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouletteBetRepository extends JpaRepository<RouletteBet, Long> {
    List<RouletteBet> findByRoulette_Id(Long rouletteId);
    List<RouletteBet> findByUserId(Long userId);
    List<RouletteBet> findByRoulette_IdAndSettled(Long soratId, Boolean settled);
    List<RouletteBet> findByUserIdAndRoulette_Id(Long userId, Long rouletteId);
}
