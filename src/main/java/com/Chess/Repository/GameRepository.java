package com.Chess.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Chess.Model.Game;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {
    public Game findByTournamentId(String tournamentId);
    public List<Game> findByStatus(String status);
}
