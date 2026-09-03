package com.project.AUTHSERVICE.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.AUTHSERVICE.Repository.UserDetRepo;

@Service
public class UserDetService implements UserDetailsService {
    

    final UserDetRepo userDetRepo;

    UserDetService(UserDetRepo userDetRepo) {
        this.userDetRepo = userDetRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
    }

    
    
}
