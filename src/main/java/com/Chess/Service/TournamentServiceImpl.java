package com.Chess.Service;

import com.Chess.Controller.LichessApiController;
import com.Chess.Model.Game;
import com.Chess.Model.User;
import com.Chess.Repository.GameRepository;
import com.Chess.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TournamentServiceImpl implements TournamentService {
    @Autowired
    private LichessApiController lichessApiController;
    @Autowired
    private CoinService coinService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    GameRepository gameRepository;

    private final RestTemplate restTemplate;

    public TournamentServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String enterTournament(User user, Float betAmount) throws Exception {
        try{// Validate user has lichess ID and sufficient coins
            if (user.getLiChessId() == null) {
                throw new Exception("Lichess ID required");
            }
            if (user.getCoins() < betAmount) {
                throw new Exception("Insufficient coins");
            }

            // Deduct coins
            coinService.deductCoins(user.getEmail(), betAmount.intValue());

            // Create Lichess tournament
            List<Game> game = gameRepository.findByStatus("open");
            System.out.println(game);

            for (Game g : game) {
                //if (g.getStatus().equals("open")) {
                    if (checkTournament(g)==0) {
                        g.setStatus("closed");
                        gameRepository.save(g);
                    }
                    if (g.getPlayerLimit() > 0) {
                        g.setPlayerLimit(g.getPlayerLimit() - 1);
                        gameRepository.save(g);
                        return g.getTournamentId();
                    } else {
                        g.setStatus("closed");
                        gameRepository.save(g);
                    }

                //}

            }
            return game.getLast().getTournamentId();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public int checkTournament( Game g){
            String url = "https://lichess.org/api/tournament/" + g.getTournamentId();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer lip_LIpPSUoXrDlXUedkNOaY"); // Replace with your actual token
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("reeeeeeeeeeeeeeeeeeeeeeeeeeeeeee="+response);
        String responseBody = response.getBody();
        // String isFinished = response.getBody().split("\"secondsToFinish\":\"")[1].split("\"")[0];
            //System.out.println("wewwwwwwwwwwww"+isFinished);
            //int secondsToFinish = Integer.parseInt(isFinished);
            //System.out.println("SSSSSSSSSSSSSSSSSSSSS="+isFinished);
            return JsonPath.read(responseBody, "$.secondsToFinish");
    }

    @Override
    public void distributePrize(String tournamentId) throws Exception {
        // Fetch tournament results from Lichess
        ResponseEntity<String> resultsResponse = lichessApiController.getTournamentResults(tournamentId);

        // Verify winner's position
        if (isFirstPlace(resultsResponse.getBody())) {
            Float originalBetAmount = getOriginalBetAmount(tournamentId);
            Float prize = originalBetAmount * 10;

            coinService.addCoins(winner.getEmail(), prize.intValue());
        }
    }

    private boolean isFirstPlace(String resultsJson) {
        String url = "https://lichess.org/api/tournament/" + g.getTournamentId() +"results";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer lip_LIpPSUoXrDlXUedkNOaY"); // Replace with your actual token
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        // Implement logic to check if lichessId is in first place
        //return resultsJson.contains(lichessId) && resultsJson.indexOf(lichessId) == 0;
        return false;
    }

    private Float getOriginalBetAmount(String tournamentId) {
        // Retrieve and return original bet amount for this tournament
        // You'll need to implement storage mechanism for bet tracking
        return 0f;
    }

    private String extractTournamentId(String responseBody) {
        // Extract tournament ID from Lichess API response
        return responseBody.split("\"id\":\"")[1].split("\"")[0];
    }
}