package com.finalproject.Tiket.Pesawat.security.service;

import com.finalproject.Tiket.Pesawat.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Log4j2
public class UserDetailsImpl implements UserDetails {

    private String emailAddress;
    private String password;

    private UUID userId;
    private List<GrantedAuthority> authorities;

    public UserDetailsImpl(String emailAddress, String password, List<GrantedAuthority> authorities, UUID userId) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.authorities = authorities;
        this.userId = userId;
    }

    public static UserDetails build(User user) {
        String roleName = user.getRole().getRoleName().toString();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleName));
        return new UserDetailsImpl(user.getEmailAddress(), user.getPassword(), authorities, user.getUuid());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return emailAddress;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}