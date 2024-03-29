package com.finalproject.Tiket.Pesawat.security;

import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.model.EnumRole;
import com.finalproject.Tiket.Pesawat.model.Images;
import com.finalproject.Tiket.Pesawat.model.Role;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.RoleRepository;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
import com.finalproject.Tiket.Pesawat.utils.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Log4j2
public class CustomOauth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${aeroswift.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        log.info(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());

        if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            log.info(attributes);
            String email = attributes.get("email").toString();


            userRepository.findByEmailAddress(email)
                    .ifPresentOrElse(user -> {
                                try {
                                    log.info("handle existing user");
                                    handleExistingUser(user, principal, response, request);
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                    throw new ExceptionHandling(e.getMessage());
                                }
                            },
                            () -> {
                                try {
                                    handleNewUser(principal, response, request);
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                    throw new RuntimeException(e);
                                }
                            });
        }

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontendUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void handleExistingUser(User user, OAuth2User oauth2User, HttpServletResponse response, HttpServletRequest request) throws IOException {
        UserDetails userDetails = UserDetailsImpl.build(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
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
    }
}
