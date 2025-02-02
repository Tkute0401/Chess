package com.Chess.Repository;

import com.Chess.Model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet,Long> {
    public List<Bet> findByGameId(Long gameId);
}
