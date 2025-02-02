package com.Chess.Config;
import com.Chess.Controller.LichessApiController;
import com.Chess.Controller.TournamentController;
import com.Chess.Model.Game;
import com.Chess.Repository.GameRepository;
import com.Chess.Service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TournamentScheduler {

    @Autowired
    private LichessApiController lichessApiController;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    TournamentService tournamentService;

    @Autowired
    TournamentController tournamentController;

    // Schedule the task to run every hour (3600000 milliseconds)
    @Scheduled(fixedRate = 7200000)
    public void scheduleCreateTournament() {
        try {

            for(int i = 0; i < 1; i++) {
                //ResponseEntity<String> response = lichessApiController.createTournament();
                //System.out.println("Tournament created: " + response.getBody());
                wait(600000);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace().getClass());
            System.err.println("Failed to create tournament: " + e.getMessage());
        }
    }
    @Scheduled(fixedRate = 13000)
    public void scheduleDistributePrize() {
        List<Game> games = gameRepository.findByStatus("open");
        try{
            for (Game game : games) {
                if (tournamentService.checkTournament(game) <= 10) {
                    tournamentController.claimPrize(Map.of("tournamentId", game.getTournamentId()));
                    game.setStatus("closed");
                    gameRepository.save(game);
                    System.out.println("price distributed");
                }
            }
        }
        catch (Exception e) {
            System.err.println("Failed to distribute prize: " + e.getMessage());
        }
    }
}