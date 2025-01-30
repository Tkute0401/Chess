package com.Chess.Request;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class TournamentEntryRequest {
    @NotNull
    private String lichessId;

    @NotNull
    private Float betAmount;
}