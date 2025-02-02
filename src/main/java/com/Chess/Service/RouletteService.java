package com.Chess.Service;

import com.Chess.Model.Bet;
import com.Chess.Model.Better;
import com.Chess.Model.Roulette;
import com.Chess.Model.User;

import java.util.List;

public interface RouletteService {
    Roulette createNewRoulette();
    void placeBet(User user, Roulette roulette, List<Better> bets) throws Exception;
    void spinWheel(Long rouletteId) throws Exception;
    void distributePrizes(Long rouletteId) throws Exception;
    Roulette getGameStatus(Long rouletteId) throws Exception;
}
