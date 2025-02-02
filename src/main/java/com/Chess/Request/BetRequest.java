package com.Chess.Request;

import com.Chess.Model.Bet;
import com.Chess.Model.Better;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetRequest {
    List<Better> bet;
    private Long gameId;
    private String email;
}
