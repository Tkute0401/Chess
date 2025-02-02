package com.Chess.Controller;

import com.Chess.Model.*;
import com.Chess.Repository.RouletteRepository;
import com.Chess.Repository.SoratRepository;
import com.Chess.Request.BetRequest;
import com.Chess.Request.SoratBetRequest;
import com.Chess.Service.RouletteService;
import com.Chess.Service.SoratService;
import com.Chess.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sorat")
public class RouletteController {
    @Autowired
    private RouletteService rouletteService;

    @Autowired
    UserService userService;
    @Autowired
    private RouletteRepository rouletteRepository;

    @PostMapping("/game/create")
    public ResponseEntity<?> createGame() {
        try {
            Roulette roulette = rouletteService.createNewRoulette();
            return ResponseEntity.ok(roulette);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/bet")
    public ResponseEntity<?> placeBet(@RequestBody BetRequest payload) {
        try {
            System.out.println("Hellle aapppa placebet");

            // Extract bet details from payload
            Long gameId = payload.getGameId();
            System.out.println("payload"+payload);
            List<Better> bet = payload.getBet();


            Roulette roulette = rouletteService.getGameStatus(gameId);

            User user = userService.findUserByEmail((String) payload.getEmail());

            rouletteService.placeBet(user, roulette,bet);
            System.out.println();
            return ResponseEntity.ok(roulette);
        } catch (Exception e) {
            System.err.println("ERRRRRRRRRRRRRRRRRRrrr"+e);
            System.err.println("AAAAAAA"+e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/spin/{gameId}")
    public ResponseEntity<?> spinWheel(@PathVariable Long gameId) {
        try {
            rouletteService.spinWheel(gameId);
            Roulette roulette = rouletteRepository.findRouletteById(gameId);
            return ResponseEntity.ok(roulette);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<?> getGameStatus(@PathVariable Long gameId) {
        try {
            Roulette game = rouletteService.getGameStatus(gameId);
            return ResponseEntity.ok(game);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
