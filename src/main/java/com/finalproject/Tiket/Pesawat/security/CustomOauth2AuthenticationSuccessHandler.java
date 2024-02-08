package com.finalproject.Tiket.Pesawat.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.Tiket.Pesawat.model.EnumRole;
import com.finalproject.Tiket.Pesawat.model.Images;
import com.finalproject.Tiket.Pesawat.model.Role;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.payload.response.JwtResponse;
import com.finalproject.Tiket.Pesawat.repository.RoleRepository;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import com.finalproject.Tiket.Pesawat.security.jwt.JwtUtils;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
import com.finalproject.Tiket.Pesawat.utils.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@Log4j2
public class CustomOauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();

            User user = userRepository.findByEmailAddress(oauth2User.getAttribute("email").toString()).orElse(null);

            if (user != null) {
                handleExistingUser(user, oauth2User, response, request);
            } else {
                handleNewUser(oauth2User, response, request);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleExistingUser(User user, OAuth2User oauth2User, HttpServletResponse response, HttpServletRequest request) throws IOException {
        UserDetails userDetails = UserDetailsImpl.build(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtUtils.generateToken(auth);
        log.info(oauth2User.toString());
        sendJwtResponse(oauth2User.getAttribute("email").toString(), token, userDetails.getAuthorities(), response, request);
    }

    private void handleNewUser(OAuth2User oauth2User, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String email = oauth2User.getAttribute("email").toString();
        String name = oauth2User.getAttributes().get("name").toString();
        String imageUrl = oauth2User.getAttributes().get("picture").toString();

        Role userRole = roleRepository.findByRoleName(EnumRole.USER).orElseThrow(()
                -> new RuntimeException("Role not found: " + EnumRole.USER));

        Images images = Images.builder()
                .name(name)
                .url(imageUrl)
                .build();

        User newUser = User.builder()
                .fullname(name)
                .emailAddress(email)
                .isActive(true)
                .role(userRole)
                .images(images)
                .createdAt(Utils.getCurrentDateTimeAsDate())
                .build();
        userRepository.save(newUser);
        log.info("Success save new user from OAuth2 Google");

        UserDetails userDetails = UserDetailsImpl.build(newUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtUtils.generateToken(auth);
        log.info(oauth2User.toString());
        sendJwtResponse(email, token, userDetails.getAuthorities(), response, request);
    }

    private void sendJwtResponse(String email, String token, Collection<? extends GrantedAuthority> authorities, HttpServletResponse response, HttpServletRequest request) throws IOException {
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(token)
                .email(email)
                .type("Bearer")
                .roles(authorities.stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList()))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(jwtResponse);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
        String redirectUrl = request.getRequestURL().toString().replace("http:", "https:");
        response.sendRedirect(redirectUrl);
        log.info(jsonResponse);
    }
}
