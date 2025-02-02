package com.Chess.Controller;

import com.Chess.Model.Game;
import com.Chess.Repository.GameRepository;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LichessApiController {

    @Autowired
    private GameRepository gameRepository;
    private final RestTemplate restTemplate;

    public LichessApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    //lip_hETHhjtmaSVVjKbK3TxJ        lip_QAKmR8tr5eeKVMqAEIA6      lip_tgWO0fw8Y5cUsy1aGxoF    lip_LIpPSUoXrDlXUedkNOaY     lip_VwpRWlbN8EFGvDWVZlMR

    @PostMapping("/tournament")
    public ResponseEntity<String> createTournament() {
        String url = "https://lichess.org/api/tournament";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer lip_QAKmR8tr5eeKVMqAEIA6"); // Replace with your valid token

        // Create body
        Map<String, Object> body = new HashMap<>();
        body.put("system", "arena");
        body.put("fullName", "Titled Arena May 2024");
        body.put("minutes", 20);
        body.put("perf", Map.of("key", "bullet", "name", "Bullet", "icon", "T"));
        body.put("clockTime", 3);
        body.put("clockIncrement", 0);
        body.put("clock", Map.of("limit", 3, "increment", 0));
        body.put("variant", "standard");
        body.put("rated", true);
        body.put("spotlight", Map.of("headline", "Titled only, $1,000 prize pool"));
        body.put("schedule", Map.of("freq", "unique", "speed", "bullet"));
        body.put("description", "Prizes: $500/$250/$125/$75/$50\r\n\r\n[Warm-up event](https://lichess.org/tournament/may24wua)");
        body.put("onlyTitled", true);

        // Log request details
        System.out.println("Headers: " + headers);
        System.out.println("Body: " + body);

        // Create entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            // Send request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String tournamentId = response.getBody().split("\"id\":\"")[1].split("\"")[0];
                Game createdGame = new Game();
                createdGame.setName(response.getBody().split("\"name\":\"")[1].split("\"")[0]);
                createdGame.setStatus("open");
                createdGame.setTournamentId(tournamentId);
                Game savedGame = gameRepository.save(createdGame);
                return response;
            } else {
                System.err.println("Failed to create tournament: " + response.getStatusCode() + " - " + response.getBody());
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    //@GetMapping("/tournament/{tournamentId}")
    //public ResponseEntity<String> getTournament(@PathVariable String tournamentId) {
    //    String url = "https://lichess.org/api/tournament/" + tournamentId;
    //    HttpHeaders headers = new HttpHeaders();
     //   headers.set("Authorization", "Bearer YOUR_ACCESS_TOKEN"); // Replace with your actual token
       // HttpEntity<String> entity = new HttpEntity<>(headers);
        //ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        //String status = response.getBody().split("\"isFinished\":\"")[1].split("\"")[0];
        //System.out.println("SSSSSSSSSSSSSSSSSSSSS="+status);
        //return response;
    //}
    @GetMapping("/tournament/{tournamentId}/results")
    public ResponseEntity<String> getTournamentResults(@PathVariable String tournamentId) {
        String url = "https://lichess.org/api/tournament/" + tournamentId + "/results";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer YOUR_ACCESS_TOKEN"); // Replace with your actual token

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String  ll = JsonPath.read(response.getBody(), "$.username");
        System.out.println("alfjafhlasjfljas"+ll);
        return response; // Returns the results as a JSON string
    }
}
