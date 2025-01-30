package com.Chess.Service;

import com.Chess.Model.User;

public interface CoinService {
    User addCoins(String email, Integer coins) throws Exception;
    User deductCoins(String email, Integer coins) throws Exception;
    float getUserCoins(String email) throws Exception;
}