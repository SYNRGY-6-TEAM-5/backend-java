package com.finalproject.Tiket.Pesawat.security.service;

import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAddress(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found : " + username));
        return UserDetailsImpl.build(user);
    }
}
