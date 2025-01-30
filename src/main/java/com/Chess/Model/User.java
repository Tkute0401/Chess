package com.Chess.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name,email,liChessId;
    private Float coins = 0.0f;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
   public void setUsers(String name,String email,String password){
        this.name=name;
        this.email=email;
        this.password=password;
        this.liChessId = liChessId;
        this.coins = coins;

    }

}