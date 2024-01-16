package com.finalproject.Tiket.Pesawat.security;


import com.finalproject.Tiket.Pesawat.model.EnumRole;
import com.finalproject.Tiket.Pesawat.model.Images;
import com.finalproject.Tiket.Pesawat.model.Role;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.RoleRepository;
import com.finalproject.Tiket.Pesawat.security.jwt.AuthEntryPointJwt;
import com.finalproject.Tiket.Pesawat.security.jwt.AuthTokenFilter;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailServiceImpl;
import com.finalproject.Tiket.Pesawat.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Optional;

@Configuration
@EnableMethodSecurity
@Log4j2
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private UserDetailServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //handle throw failed login
        HttpSessionRequestCache myReqeustCache = new HttpSessionRequestCache();
        myReqeustCache.setMatchingRequestParameterName(null);
        myReqeustCache.setCreateSessionAllowed(false);

        http
                .securityContext(context -> context.requireExplicitSave(false))
                .requestCache((cache) -> cache.requestCache(myReqeustCache))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/login/**").permitAll()
                                .requestMatchers("/error/**").permitAll()
                                .requestMatchers("/v2/**").permitAll()
                                .requestMatchers("/swagger-resources/**").permitAll()
                                .requestMatchers("/webjars/**").permitAll()
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/swagger-ui.html").permitAll()
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .anyRequest()
                                .authenticated())
                .httpBasic(basic -> basic.authenticationEntryPoint(unauthorizedHandler))
                .exceptionHandling(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth ->
                                oauth.successHandler((request, response, authentication) -> {
                                    DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
//                                    Optional<Role> optionalUserRole = roleRepository.findByRoleName(EnumRole.USER);

                                    User user = User.builder()
                                            .emailAddress(oidcUser.getAttribute("email"))
                                            .fullname(oidcUser.getAttribute("name"))
                                            .isActive(true)
//                                            .role(optionalUserRole.get())
                                            .build();
                                    Boolean saveUser = userService.saveNewUserFromOauth2(user, oidcUser.getAttribute("picture"));

                                    if (saveUser) {
                                        log.info("sukses create user");
                                    } else {
                                        log.info("failed create user");
                                    }
                                    log.info(oidcUser.getAttribute("name") + " " + oidcUser.getAttribute("email"));
                                })
                                        .defaultSuccessUrl("/api/v1/auth/user", true) // todo handle redirect URL
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthTokenFilter authJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
