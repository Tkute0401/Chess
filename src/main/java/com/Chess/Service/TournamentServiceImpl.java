package com.Chess.Service;

import com.Chess.Controller.LichessApiController;
import com.Chess.Model.Bet;
import com.Chess.Model.Game;
import com.Chess.Model.User;
import com.Chess.Repository.BetRepository;
import com.Chess.Repository.GameRepository;
import com.Chess.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import com.jayway.jsonpath.JsonPath;
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
    @Autowired
    BetRepository betRepository;
    private final RestTemplate restTemplate;
    @Autowired
    private UserService userService;

    public TournamentServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String enterTournament(User user, Float betAmount) throws Exception {
        try {// Validate user has lichess ID and sufficient coins
            if (user.getLiChessId() == null) {
                throw new Exception("Lichess ID required");
            }
            if (user.getCoins() < betAmount) {
                throw new Exception("Insufficient coins");
            }

    
            // Create Lichess tournament
            List<Game> game = gameRepository.findByStatus("open");
            System.out.println(game);

            for (Game g : game) {
                //if (g.getStatus().equals("open")) {
                if (checkTournament(g) == 0) {
                    g.setStatus("closed");
                    gameRepository.save(g);
                }
                if (g.getPlayerLimit() > 0) {
                    g.setPlayerLimit(g.getPlayerLimit() - 1);
                    gameRepository.save(g);
                    Bet bet = new Bet();
                    bet.setGame(g);
                    bet.setUser(user);
                    bet.setBetAmount(betAmount);
                    betRepository.save(bet);
                    coinService.deductCoins(user.getEmail(), betAmount.intValue());
                    return g.getTournamentId();
                } else {
                    g.setStatus("closed");
                    gameRepository.save(g);
                }

                //}

            }
            return game.getLast().getTournamentId();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public int checkTournament(Game g) throws Exception {
        String url = "https://lichess.org/api/tournament/" + g.getTournamentId();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer lip_LIpPSUoXrDlXUedkNOaY"); // Replace with your actual token
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("reeeeeeeeeeeeeeeeeeeeeeeeeeeeeee=" + response);
            String responseBody = response.getBody();
            if(responseBody.contains("isFinished")){
                g.setStatus("closed");
                gameRepository.save(g);
            }
            if(responseBody.contains("secondsToStart")) {
                int seconds = JsonPath.read(responseBody, "$.secondsToStart");
                System.out.println("SECONDS TO START=" + seconds);
                if(seconds <= 10) {
                    g.setStatus("open");
                    gameRepository.save(g);
                }
            }
            if(responseBody.contains("secondsToFinish")) {
            boolean isStarted = JsonPath.read(responseBody, "$.isStarted");
            if (isStarted == true) {
                int secondsToFinish = JsonPath.read(responseBody, "$.secondsToFinish");
                System.out.println("SECONDS TO FINISH=" + secondsToFinish);
                return secondsToFinish;

            }else {
                return 90000;
            }
            }else {
                return 900000;
            }

    }

    @Override
    public void distributePrize(String tournamentId) throws Exception {
        System.out.println("entered distribute prize");
        // Fetch tournament results from Lichess
        try{
            System.out.println("entered try");
            ResponseEntity<String> resultsResponse = lichessApiController.getTournamentResults(tournamentId);
            String lichessId = JsonPath.read(resultsResponse.getBody(), "$.username");
            if(JsonPath.read(resultsResponse.getBody(), "$.score").equals("0")) {
                User winner = userRepository.findByLiChessId(lichessId);
                System.out.println(winner);
                // Verify winner's position
                Float originalBetAmount = getOriginalBetAmount(tournamentId);
                Float prize = originalBetAmount * 1;

                coinService.addCoins(winner.getEmail(), prize.intValue());
            }
            else {
                System.out.println("aassaa"+resultsResponse.getBody());
                System.out.println("Lichess ID: " + lichessId);
                User winner = userRepository.findByLiChessId(lichessId);
                System.out.println(winner);
                // Verify winner's position
                Float originalBetAmount = getOriginalBetAmount(tournamentId);
                Float prize = originalBetAmount * 10;

                coinService.addCoins(winner.getEmail(), prize.intValue());
            }

        } catch (Exception e) {

            System.err.println("Failed to distribute prize: ssssss " + e.getMessage());
        }
    }


    private Float getOriginalBetAmount(String tournamentId) {
        Game game = gameRepository.findByTournamentId(tournamentId);

        // Retrieve and return original bet amount for this tournament
        List<Bet> bet = betRepository.findByGameId(game.getId());

        // You'll need to implement storage mechanism for bet tracking

        return bet.get(0).getBetAmount();
    }

    private String extractTournamentId(String responseBody) {
        // Extract tournament ID from Lichess API response
        return responseBody.split("\"id\":\"")[1].split("\"")[0];
    }
}