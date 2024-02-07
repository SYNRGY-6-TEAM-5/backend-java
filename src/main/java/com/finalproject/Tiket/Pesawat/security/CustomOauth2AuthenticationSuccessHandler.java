package com.finalproject.Tiket.Pesawat.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.payload.response.JwtResponse;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import com.finalproject.Tiket.Pesawat.security.jwt.JwtUtils;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.stream.Collectors;

@Configuration
@Log4j2
public class CustomOauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();

            User user = userRepository.findByEmailAddress(oauth2User.getAttribute("email").toString()).orElse(null);

            if (user != null) {
                UserDetails userDetails = UserDetailsImpl.build(user);
                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
                String token = jwtUtils.generateToken(auth);
                log.info(oauth2User.toString());
                String email = oauth2User.getAttribute("email").toString();
                JwtResponse jwtResponse = JwtResponse.builder()
                        .token(token)
                        .email(email)
                        .type("Bearer")
                        .roles(userDetails.getAuthorities().stream()
                                .map(item -> item.getAuthority())
                                .collect(Collectors.toList()))
                        .build();

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(jwtResponse);

                response.setContentType("application/json");
                response.getWriter().write(jsonResponse);
                response.getWriter().flush();
                log.info(jsonResponse);
            } else {
                String email = oauth2User.getAttribute("email").toString();
                String name = oauth2User.getAttributes().get("name").toString();
                String imageUrl = oauth2User.getAttributes().get("picture").toString();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
