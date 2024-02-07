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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.finalproject.Tiket.Pesawat.utils.Constants.CONSTANT_EMAIL_TEST_FORGOT;

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

            if (loginDto.getEmailAddress().equals(CONSTANT_EMAIL_TEST_FORGOT)) {
                return ResponseEntity.ok(OTPValidationResponse.builder()
                        .status(true)
                        .message("Success Login Dummy User")
                        .token(jwtUtils.generateDummyToken())
                        .build());
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmailAddress(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            return ResponseEntity.ok(JwtResponse.builder()
                    .token(jwt)
                    .type("Bearer")
                    .email(userDetails.getUsername())
                    .roles(userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .build());
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
            @Valid @RequestBody OTPValidationRequest validationRequest
    ) {
        ValidSignUpResponse response = otpService.validateOTPRegister(validationRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> generateOTP(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        ForgotPasswordResponse response = authService.forgotPasswordUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password/validate-otp")
    public ResponseEntity<Object> forgotPasswordValidateOTP(
            @Valid @RequestBody OTPValidationRequest validationRequest
    ) {
        OTPValidationResponse response = otpService.validateOTPForgotPassword(validationRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/forgot-password/edit-password")
    public ResponseEntity<Object> editPasswordUser(
            @Valid @RequestBody RequestEditUser requestEditUser) {
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
