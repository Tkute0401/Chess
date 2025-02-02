package com.Chess.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Roulette {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String gameId;
    private Integer winningNumber;
    private Long timestamp;
    private Float multiplier = 12.0f;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus = GameStatus.WAITING;

    public enum GameStatus {
        WAITING,
        BETTING,
        SPINNING,
        COMPLETED
    }
}
