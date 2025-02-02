package com.Chess.Repository;

import com.Chess.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public User findByEmail(String username);
    public User findByLiChessId(String liChessId);

}