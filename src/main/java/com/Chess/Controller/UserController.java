package com.Chess.Controller;

import com.Chess.Model.User;
import com.Chess.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> findUserByJwtToken(@RequestHeader("Authorization") String jwt) {
        try {
            System.out.println("Received JWT: " + jwt);  // Debug log
            User user = userService.findUserByJwtToken(jwt);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error in profile endpoint: " + e.getMessage());  // Debug log
            e.printStackTrace();  // Print full stack trace
            return new ResponseEntity<>(
                    Map.of("error", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}