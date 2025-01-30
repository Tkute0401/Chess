package com.Chess.Controller;

import com.Chess.Model.User;
import com.Chess.Service.TournamentService;
import com.Chess.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/tournament")
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private UserService userService;

    @PostMapping("/enter")
    public ResponseEntity<?> enterTournament(@RequestBody Map<String, Object> payload) {
        try {
            String email = (String) payload.get("email");
            Float betAmount = Float.parseFloat(payload.get("betAmount").toString());

            User user = userService.findUserByEmail(email);
            String tournamentId = tournamentService.enterTournament(user, betAmount);

            return ResponseEntity.ok(Map.of(
                    "tournamentLink", "https://lichess.org/tournament/" + tournamentId,
                    "tournamentId", tournamentId
            ));
        } catch (Exception e) {
            System.out.println("Aaaaaaaaaaaaaaaa"+payload);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/claim-prize")
    public ResponseEntity<?> claimPrize(@RequestBody Map<String, Object> payload) {
        try {
            String tournamentId = (String) payload.get("tournamentId");

            tournamentService.distributePrize(tournamentId);

            return ResponseEntity.ok("Prize claimed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}