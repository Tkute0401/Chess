package com.Chess.Service;
import com.Chess.Model.User;
import com.Chess.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("user not found by email"+username);
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("user");
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(), Collections.singletonList(authority));
    }

}