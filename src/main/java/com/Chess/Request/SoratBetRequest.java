package com.Chess.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoratBetRequest {
    private Long gameId;
    private String email;
    private String betType;
    private Float amount;
    private Integer number;
}
