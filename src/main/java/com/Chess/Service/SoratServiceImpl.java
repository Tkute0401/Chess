package com.Chess.Service;


import com.Chess.Model.Sorat;
import com.Chess.Model.SoratBet;
import com.Chess.Model.User;
import com.Chess.Repository.SoratBetRepository;
import com.Chess.Repository.SoratRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class SoratServiceImpl implements SoratService {
    @Autowired
    private SoratRepository soratRepository;

    @Autowired
    private SoratBetRepository soratBetRepository;

    @Autowired
    private CoinService coinService;

    @Override
    public Sorat createNewSorat() {
        Sorat sorat = new Sorat();
        sorat.setGameId(UUID.randomUUID().toString());
        sorat.setStatus("WAITING");
        sorat.setTimestamp(System.currentTimeMillis());
        return soratRepository.save(sorat);
    }

    @Override
    public void placeBet(User user, Sorat game, String betType, Float amount, Integer number) throws Exception {
        if (game.getGameStatus() != Sorat.GameStatus.BETTING) {
            throw new Exception("Game is not in betting phase");
        }

        if (user.getCoins() < amount) {
            throw new Exception("Insufficient funds");
        }

        SoratBet soratBet = new SoratBet();
        soratBet.setSorat(game);
        soratBet.setUser(user);
        soratBet.setBetAmount(amount);
        soratBet.setBetType(betType);
        soratBet.setNumber(number);

        coinService.deductCoins(user.getEmail(), amount.intValue());
        soratBetRepository.save(soratBet);
    }

    @Override
    public void spinWheel(Long gameId) throws Exception {
        Sorat game = soratRepository.findSoratById(gameId);
        if (game == null) {
            throw new Exception("Game not found");
        }

        game.setGameStatus(Sorat.GameStatus.SPINNING);
        game.setWinningNumber(1);//(new Random().nextInt(37)); // 0-36
        distributePrizes(gameId);
        soratRepository.save(game);
    }

    @Override
    public void distributePrizes(Long gameId) throws Exception {
        Sorat game = soratRepository.findSoratById(gameId);
        if (game == null) {
            throw new Exception("Game not found with ID: " + gameId);
        }

        // Fetch all bets for the game
        List<SoratBet> bets = soratBetRepository.findBySorat_Id(gameId);

        for (SoratBet bet : bets) {
            if (!bet.getSettled()) {
                float winAmount = calculateWinAmount(bet, game.getWinningNumber());
                if (winAmount > 0) {
                    coinService.addCoins(bet.getUser().getEmail(), (int) winAmount);
                }
                bet.setSettled(true);
                soratBetRepository.save(bet);
            }
        }

        game.setGameStatus(Sorat.GameStatus.COMPLETED);
        soratRepository.save(game);
    }

    private float calculateWinAmount(SoratBet bet, int winningNumber) {
        // Implementation of different bet types and their payouts
        switch (bet.getBetType()) {
            case "NUMBER":
                return bet.getNumber() == winningNumber ? bet.getBetAmount() * 35 : 0;
            case "RED":
                return isRed(winningNumber) ? bet.getBetAmount() * 2 : 0;
            case "BLACK":
                return isBlack(winningNumber) ? bet.getBetAmount() * 2 : 0;
            case "EVEN":
                return winningNumber != 0 && winningNumber % 2 == 0 ? bet.getBetAmount() * 2 : 0;
            case "ODD":
                return winningNumber != 0 && winningNumber % 2 == 1 ? bet.getBetAmount() * 2 : 0;
            default:
                return 0;
        }
    }

    private boolean isRed(int number) {
        // Implementation of red numbers in roulette
        int[] redNumbers = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        return Arrays.stream(redNumbers).anyMatch(n -> n == number);
    }

    private boolean isBlack(int number) {
        return number != 0 && !isRed(number);
    }

    @Override
    public Sorat getGameStatus(Long gameId) throws Exception {
        Sorat game = soratRepository.findSoratById(gameId);
        if (game == null) {
            throw new Exception("Game not found with ID: " + gameId);
        }
        if(game.getGameStatus()==Sorat.GameStatus.WAITING){
            if((System.currentTimeMillis()-game.getTimestamp())>10000){
                game.setGameStatus(Sorat.GameStatus.BETTING);
                soratRepository.save(game);
            }
        }
        if((System.currentTimeMillis()-game.getTimestamp())>10000){
            game.setGameStatus(Sorat.GameStatus.BETTING);
            soratRepository.save(game);
        }
        // Update game status based on current state if needed
        if ((game.getGameStatus() == Sorat.GameStatus.SPINNING) &&
                ((System.currentTimeMillis() - game.getTimestamp()) > 5000)) { // 5 seconds for spinning
            game.setGameStatus(Sorat.GameStatus.COMPLETED);
            soratRepository.save(game);
            // Trigger prize distribution
            distributePrizes(gameId);
        }

        return game;
    }
}
