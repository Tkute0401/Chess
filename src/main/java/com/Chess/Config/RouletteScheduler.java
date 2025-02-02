//package com.Chess.Config;
//
//import com.Chess.Controller.RouletteController;
//import com.Chess.Model.Roulette;
//import com.Chess.Repository.RouletteRepository;
//import com.Chess.Service.RouletteService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class RouletteScheduler {
//    @Autowired
//    RouletteService rouletteService;
//
//    @Autowired
//    RouletteRepository rouletteRepository;
//
//    @Autowired
//    RouletteController rouletteController;
//
//    @Scheduled(fixedRate = 5000)
//    public void scheduleCreateRoulette() {
//            rouletteController.createGame();
//    }
//
//    @Scheduled(fixedRate = 5000)
//    public void scheduleSpinWheel() {
//        List<Roulette> games = rouletteRepository.findByGameStatus(Roulette.GameStatus.BETTING);
//        for (Roulette game : games) {
//            try {
//                rouletteService.spinWheel(game.getId());
//            } catch (Exception e) {
//                System.err.println(e.getMessage());
//            }
//        }
//    }
//
//}
