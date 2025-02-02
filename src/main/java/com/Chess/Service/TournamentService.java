package com.Chess.Service;

import com.Chess.Model.Game;
import com.Chess.Model.User;

public interface TournamentService {
    String enterTournament(User user, Float betAmount) throws Exception;

    void distributePrize(String tournamentId) throws Exception;

    int checkTournament(Game g) throws Exception;
}