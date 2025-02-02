package com.Chess.Service;


import com.Chess.Model.*;
import com.Chess.Repository.RouletteBetRepository;
import com.Chess.Repository.RouletteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class RouletteServiceImpl implements RouletteService {
    @Autowired
    private RouletteRepository rouletteRepository;

    @Autowired
    private RouletteBetRepository rouletteBetRepository;

    @Autowired
    private CoinService coinService;

    @Override
    public Roulette createNewRoulette() {
        Roulette roulette = new Roulette();
        roulette.setGameId(UUID.randomUUID().toString());
        roulette.setGameStatus(Roulette.GameStatus.BETTING);
        roulette.setTimestamp(System.currentTimeMillis());
        return rouletteRepository.save(roulette);
    }

    @Override
    @Transactional
    public void placeBet(User user, Roulette game,  List<Better> bets) throws Exception {
        try {
            System.out.println(bets);
            if (game.getGameStatus() != Roulette.GameStatus.BETTING) {
                throw new Exception("Game is not in betting phase");
            }

            if (user.getCoins() < bets.stream().map(Better::getBetAmount).reduce(0f, Float::sum)) {
                throw new Exception("Insufficient funds");
            }

            RouletteBet rouletteBet = new RouletteBet();
            rouletteBet.setRoulette(game);
            rouletteBet.setUser(user);
            rouletteBet.setBets(bets);

            coinService.deductCoins(user.getEmail(), bets.stream().map(Better::getBetAmount).reduce(0f, Float::sum).intValue());
            rouletteBetRepository.save(rouletteBet);
        }
        catch (Exception e) {
            System.err.println("LLLALLLALLA"+e);
            throw new Exception(e.getMessage());
        }

    }

    @Override
        public void spinWheel(Long gameId) throws Exception {

                Roulette game = rouletteRepository.findRouletteById(gameId);
                if (game == null) {
                    throw new Exception("Game not found");
                }
                if(game.getGameStatus() == Roulette.GameStatus.WAITING)
                    throw new Exception("Game is not in betting phase");
                if(game.getGameStatus()==Roulette.GameStatus.COMPLETED)
                    throw new Exception("Game is already completed");
                if (game.getGameStatus() == Roulette.GameStatus.BETTING) {
                    game.setGameStatus(Roulette.GameStatus.SPINNING);
                    game.setWinningNumber(new Random().nextInt(12)); // 0-36
                    distributePrizes(gameId);
                    rouletteRepository.save(game);
                }
    }

    @Override
    public void distributePrizes(Long gameId) throws Exception {
        Roulette game = rouletteRepository.findRouletteById(gameId);
        if (game == null) {
            throw new Exception("Game not found with ID: " + gameId);
        }

        // Fetch all bets for the game
        List<RouletteBet> bets = rouletteBetRepository.findByRoulette_Id(gameId);

        for (RouletteBet bet : bets) {
            if (!bet.getSettled()) {
                float winAmount = calculateWinAmount(bet, game.getWinningNumber());
                if (winAmount > 0) {
                    coinService.addCoins(bet.getUser().getEmail(), (int) winAmount);
                }
                bet.setSettled(true);
                rouletteBetRepository.save(bet);
            }
        }

        game.setGameStatus(Roulette.GameStatus.COMPLETED);
        rouletteRepository.save(game);
    }

    private float calculateWinAmount(RouletteBet bet, int winningNumber) {
        // Implementation of different bet types and their payouts
        for (Better b : bet.getBets())
            switch (b.getBetType()) {
                case "NUMBER":
                    for (Better c : bet.getBets()) {
                        return c.getNumber() == winningNumber ? c.getBetAmount() * 10 : 0;
                    }
                case "RED":
                    for (Better h : bet.getBets()) {
                        return isRed(winningNumber) ? h.getBetAmount() * 2 : 0;
                    }
                case "BLACK":
                    for (Better h : bet.getBets())
                        return isBlack(winningNumber) ? h.getBetAmount() * 2 : 0;
                case "EVEN":
                    for (Better h : bet.getBets())
                        return winningNumber != 0 && winningNumber % 2 == 0 ? h.getBetAmount() * 2 : 0;
                case "ODD":
                    for (Better h : bet.getBets())
                        return winningNumber != 0 && winningNumber % 2 == 1 ? h.getBetAmount() * 2 : 0;
                default:
                    return 0;
            }
        return 0;
    }

    private boolean isRed(int number) {
        // Implementation of red numbers in roulette
        int[] redNumbers = {1, 3, 5, 7, 9, 12};
        return Arrays.stream(redNumbers).anyMatch(n -> n == number);
    }

    private boolean isBlack(int number) {
        return number != 0 && !isRed(number);
    }

    @Override
    public Roulette getGameStatus(Long gameId) throws Exception {
        Roulette game = rouletteRepository.findRouletteById(gameId);
        if (game == null) {
            throw new Exception("Game not found with ID: " + gameId);
        }
        if(game.getGameStatus()==Roulette.GameStatus.WAITING){
            if((System.currentTimeMillis()-game.getTimestamp())>10000){
                game.setGameStatus(Roulette.GameStatus.BETTING);
                rouletteRepository.save(game);
            }
        }
        if((System.currentTimeMillis()-game.getTimestamp())>10000){
            game.setGameStatus(Roulette.GameStatus.BETTING);
            rouletteRepository.save(game);
        }
        // Update game status based on current state if needed
        if ((game.getGameStatus() == Roulette.GameStatus.SPINNING) &&
                ((System.currentTimeMillis() - game.getTimestamp()) > 5000)) { // 5 seconds for spinning
            game.setGameStatus(Roulette.GameStatus.COMPLETED);
            rouletteRepository.save(game);
            // Trigger prize distribution
            distributePrizes(gameId);
        }

        return game;
    }
}
