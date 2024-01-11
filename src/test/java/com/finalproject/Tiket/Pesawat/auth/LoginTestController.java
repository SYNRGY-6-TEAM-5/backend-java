//package com.finalproject.Tiket.Pesawat.auth;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.finalproject.Tiket.Pesawat.controller.AuthController;
//import com.finalproject.Tiket.Pesawat.payload.dto.auth.LoginDto;
//import com.finalproject.Tiket.Pesawat.repository.RoleRepository;
//import com.finalproject.Tiket.Pesawat.repository.UserRepository;
//import com.finalproject.Tiket.Pesawat.security.jwt.JwtUtils;
//import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith({MockitoExtension.class, SpringExtension.class})
//@WebMvcTest(AuthController.class)
//public class LoginTestController {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthenticationManager authenticationManager;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @MockBean
//    private RoleRepository roleRepository;
//
//    @MockBean
//    private PasswordEncoder encoder;
//
//    @MockBean
//    private JwtUtils jwtUtils;
//
//    @Test
//    public void testLogin() throws Exception {
//        LoginDto loginDto = new LoginDto("test@gmail.com", "12345");
//        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
//        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getEmailAddress(), loginDto.getPassword(), authorities);
//        UserDetailsImpl userDetails = new UserDetailsImpl("test@gmail.com", "12345", authorities);
//
//        when(authenticationManager.authenticate(ArgumentMatchers.any(Authentication.class)))
//                .thenReturn(authentication);
//        when(jwtUtils.generateToken(ArgumentMatchers.any(Authentication.class)))
//                .thenReturn("mockedJwtToken");
//
//// Call the method directly on the real object
//        UserDetails principal = (UserDetails) authentication.getPrincipal();
//// Now stub the behavior
//        when(principal.getUsername())
//                .thenReturn(userDetails.getUsername());
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(loginDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("mockedJwtToken"))
//                .andExpect(jsonPath("$.username").value("test@gmail.com"))
//                .andExpect(jsonPath("$.roles").isArray());
//
//        verify(authenticationManager, times(1)).authenticate(ArgumentMatchers.any(Authentication.class));
//        verify(jwtUtils, times(1)).generateToken(ArgumentMatchers.any(Authentication.class));
//    }
//}