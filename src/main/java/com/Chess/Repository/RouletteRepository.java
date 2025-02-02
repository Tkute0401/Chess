package com.Chess.Repository;

import com.Chess.Model.Roulette;
import com.Chess.Model.Sorat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouletteRepository extends JpaRepository<Roulette, Long> {
    Roulette findRouletteById(Long id);
    List<Roulette> findByGameStatus(Roulette.GameStatus status);
}
