package com.CricketBetting.Cricket.controller;

import com.CricketBetting.Cricket.domain.entity.User;
import com.CricketBetting.Cricket.domain.request.LoginRequestDto;
import com.CricketBetting.Cricket.domain.request.UserRequestDto;
import com.CricketBetting.Cricket.domain.response.LoginResponseDto;
import com.CricketBetting.Cricket.domain.response.UserResponseDto;
import com.CricketBetting.Cricket.repository.UserRepository;
import com.CricketBetting.Cricket.security.CustomUserDetailsService;
import com.CricketBetting.Cricket.security.JwtUtil;
import com.CricketBetting.Cricket.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.createUser(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDto(token, user.getEmail(), user.getRole().name(), user.getId()));
    }

}