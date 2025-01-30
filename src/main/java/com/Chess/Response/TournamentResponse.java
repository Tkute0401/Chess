package com.Chess.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TournamentResponse {
    private String tournamentId;
    private Float betAmount;
    private String message;
}