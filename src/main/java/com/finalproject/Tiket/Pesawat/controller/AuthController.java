package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.auth.request.ForgotPasswordRequest;
import com.finalproject.Tiket.Pesawat.dto.auth.request.RequestEditUser;
import com.finalproject.Tiket.Pesawat.dto.auth.request.SignUpRequest;
import com.finalproject.Tiket.Pesawat.dto.auth.response.ForgotPasswordResponse;
import com.finalproject.Tiket.Pesawat.dto.auth.response.ResponseEditPassword;
import com.finalproject.Tiket.Pesawat.dto.auth.response.ValidSignUpResponse;
import com.finalproject.Tiket.Pesawat.dto.otp.OTPValidationRequest;
import com.finalproject.Tiket.Pesawat.dto.otp.response.OTPValidationResponse;
import com.finalproject.Tiket.Pesawat.dto.otp.response.SignUpResponse;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.payload.dto.auth.LoginDto;
import com.finalproject.Tiket.Pesawat.payload.response.JwtResponse;
import com.finalproject.Tiket.Pesawat.security.jwt.JwtUtils;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
import com.finalproject.Tiket.Pesawat.service.AuthService;
import com.finalproject.Tiket.Pesawat.service.OTPService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthService authService;

    @Autowired
    private OTPService otpService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmailAddress(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getUsername(),
                    roles));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedHandling("Failed Login, Wrong Email or Password");
        }
    }



    // sign up user
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        SignUpResponse response = authService.signUpUser(signUpRequest);
        if (!response.getSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup/validate-otp")
    public ResponseEntity<Object> signUpvalidateOTP(
            @RequestBody OTPValidationRequest validationRequest
    ) {
        ValidSignUpResponse response = otpService.validateOTPRegister(validationRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> generateOTP(
            @RequestBody ForgotPasswordRequest request
    ) {
        ForgotPasswordResponse response = authService.forgotPasswordUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password/validate-otp")
    public ResponseEntity<Object> forgotPasswordValidateOTP(
            @RequestBody OTPValidationRequest validationRequest
    ) {
        OTPValidationResponse response = otpService.validateOTPForgotPassword(validationRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit-password")
    public ResponseEntity<Object> editPasswordUser(
            @Valid @RequestBody RequestEditUser requestEditUser
    ) {
        ResponseEditPassword response = authService.editPassUser(requestEditUser);
        return ResponseEntity.ok(response);
    }


    // OAUTH2
    // todo handle success or error
//    @GetMapping("/user")
//    public Map<String, Object> userOauth2() {
//        Map<String, Object> response = new HashMap<>();
//
//        response.put("message", "success register with oauth2");
//        return response;
//    }

}
