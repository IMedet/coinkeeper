package com.app.coinkeeper.service;

import com.app.coinkeeper.config.JwtTokenUtil;
import com.app.coinkeeper.dto.AuthRequestDto;
import com.app.coinkeeper.dto.AuthResponseDto;
import com.app.coinkeeper.dto.RegisterRequestDto;
import com.app.coinkeeper.entity.User;
import com.app.coinkeeper.repository.UserRepository;
import com.app.coinkeeper.validations.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public AuthResponseDto login(@RequestBody AuthRequestDto loginRequest) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtTokenUtil.generateToken(userDetails);

            User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

            return new AuthResponseDto(jwt, "Bearer", user.getId(), user.getUsername());
    }

    public AuthResponseDto register(RegisterRequestDto registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(registerRequest.getUsername())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();

        String jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthResponseDto(jwt, "Bearer", user.getId(), user.getUsername());
    }
}