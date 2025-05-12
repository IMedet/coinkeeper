package com.app.coinkeeper.controller;


import com.app.coinkeeper.dto.AuthRequestDto;
import com.app.coinkeeper.dto.AuthResponseDto;
import com.app.coinkeeper.dto.RegisterRequestDto;
import com.app.coinkeeper.dto.ResponseDto;
import com.app.coinkeeper.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout() {
        return ResponseEntity.ok(new ResponseDto("Successfully logged out"));
    }
}
