package com.Chess.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoratBet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Sorat_id")
    private Sorat sorat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Float betAmount;
    private String betType; // NUMBER, RED, BLACK, EVEN, ODD, etc.
    private Integer number; // For direct number bets
    private Boolean settled = false;
}
