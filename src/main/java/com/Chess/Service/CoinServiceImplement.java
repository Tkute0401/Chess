package com.Chess.Service;

import com.Chess.Model.User;
import com.Chess.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinServiceImplement implements CoinService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addCoins(String email, Integer coins) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found with email: " + email);
        }

        // Add new coins to existing coins
        Float currentCoins = user.getCoins() != null ? user.getCoins() : 0;
        user.setCoins(currentCoins + coins);

        return userRepository.save(user);
    }

    @Override
    public User deductCoins(String email, Integer coins) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found with email: " + email);
        }
        // Add new coins to existing coins
        Float currentCoins = user.getCoins() != null ? user.getCoins() : 0;
        user.setCoins(currentCoins - coins);

        return userRepository.save(user);
    }


    @Override
    public float getUserCoins(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found with email: " + email);
        }
        return user.getCoins() != null ? user.getCoins() : 0;
    }
}