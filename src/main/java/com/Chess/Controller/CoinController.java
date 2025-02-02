package com.Chess.Controller;

import com.Chess.Model.User;
import com.Chess.Request.CoinRequest;
import com.Chess.Service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @PostMapping("/add")
    public ResponseEntity<?> addCoins(@RequestBody CoinRequest request) {
        try {
            User updatedUser = coinService.addCoins(request.getEmail(), request.getCoins());

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Failed to add coins: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deduct")
    public ResponseEntity<?> deductCoins(@RequestBody CoinRequest request) {
        try {
            User updatedUser = coinService.deductCoins(request.getEmail(), request.getCoins());
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/balance/{email}")
    public ResponseEntity<?> getCoins(@PathVariable String email) {
        try {
            Float coins = coinService.getUserCoins(email);
            return new ResponseEntity<>(coins, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}