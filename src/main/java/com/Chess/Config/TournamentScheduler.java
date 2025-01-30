package com.Chess.Config;
import com.Chess.Controller.LichessApiController;
import com.Chess.Model.Game;
import com.Chess.Repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TournamentScheduler {

    @Autowired
    private LichessApiController lichessApiController;

    @Autowired
    GameRepository gameRepository;

    // Schedule the task to run every hour (3600000 milliseconds)
    @Scheduled(fixedRate = 60000)
    public void scheduleCreateTournament() {
        try {

            for(int i = 0; i < 1; i++) {
                //ResponseEntity<String> response = lichessApiController.createTournament();
                //System.out.println("Tournament created: " + response.getBody());
                System.out.println("Tournament created");
            }
        } catch (Exception e) {
            System.err.println("Failed to create tournament: " + e.getMessage());
        }
    }
}