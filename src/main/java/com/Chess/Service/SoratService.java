package com.Chess.Service;

import com.Chess.Model.Sorat;
import com.Chess.Model.User;

public interface SoratService {
    Sorat createNewSorat();
    void placeBet(User user, Sorat sorat, String betType, Float amount, Integer number) throws Exception;
    void spinWheel(Long soratId) throws Exception;
    void distributePrizes(Long soratId) throws Exception;
    Sorat getGameStatus(Long soratId) throws Exception;
}
