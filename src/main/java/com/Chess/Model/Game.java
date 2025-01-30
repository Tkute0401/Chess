package com.Chess.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String tournamentId;
    private float playerLimit = 20f;
    private String status = "open";

    public Game(String  name, String status, String tournamentId) {
        this.name = name;
        this.status = status;
        this.tournamentId = tournamentId;
    }
}
