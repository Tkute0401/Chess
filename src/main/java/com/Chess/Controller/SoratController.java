package com.Chess.Controller;


import com.Chess.Model.Sorat;
import com.Chess.Model.User;
import com.Chess.Repository.SoratRepository;
import com.Chess.Request.SoratBetRequest;
import com.Chess.Service.SoratService;
import com.Chess.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/roulette")
public class SoratController {
    @Autowired
    private SoratService soratService;

    @Autowired
    UserService userService;
    @Autowired
    private SoratRepository soratRepository;

    @PostMapping("/game/create")
    public ResponseEntity<?> createGame() {
        try {
            Sorat sorat = soratService.createNewSorat();
            return ResponseEntity.ok(sorat);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/bet")
    public ResponseEntity<?> placeBet(@RequestBody SoratBetRequest payload) {
        try {
            // Extract bet details from payload
            Long gameId = payload.getGameId();
            String betType = payload.getBetType();
            Float amount = payload.getAmount();
            Integer number = payload.getNumber() != null ?
                    Integer.valueOf(payload.getNumber().toString()) : null;

            Sorat sorat = soratService.getGameStatus(gameId);

            User user = userService.findUserByEmail((String) payload.getEmail());

            soratService.placeBet(user, sorat, betType, amount, number);

            return ResponseEntity.ok(sorat);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/spin/{gameId}")
    public ResponseEntity<?> spinWheel(@PathVariable Long gameId) {
        try {
            soratService.spinWheel(gameId);
            Sorat sorat = soratRepository.findSoratById(gameId);
            return ResponseEntity.ok(sorat);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<?> getGameStatus(@PathVariable Long gameId) {
        try {
            Sorat game = soratService.getGameStatus(gameId);
            return ResponseEntity.ok(game);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
