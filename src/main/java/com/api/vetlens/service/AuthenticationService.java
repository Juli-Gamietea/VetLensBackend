package com.api.vetlens.service;

import com.api.vetlens.dto.authentication.AuthenticationRequestDTO;
import com.api.vetlens.dto.authentication.AuthenticationResponseDTO;
import com.api.vetlens.dto.user.UserRequestDTO;
import com.api.vetlens.entity.Role;
import com.api.vetlens.entity.User;
import com.api.vetlens.exceptions.ApiException;
import com.api.vetlens.exceptions.UserAlreadyExistsException;
import com.api.vetlens.repository.UserRepository;
import com.api.vetlens.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO register(UserRequestDTO request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if(userOptional.isPresent()){
            throw new UserAlreadyExistsException("El nombre de usuario " + request.getUsername() + " se encuentra en uso");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .licenseNumber(request.getLicenseNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        if (request.getRole().equals("VET")) {
            user.setRole(Role.VET);
        }else{
           user.setRole(Role.DEFAULT);
        }
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        userService.sendEmailNewAccount(request.getEmail(), request.getPassword(), request.getUsername());
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponseDTO login(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("entre");
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        System.out.println(refreshToken);
        username = jwtService.extractUsername(refreshToken);
        System.out.println(username);

        if (username != null) {
            var user = this.userRepository.findByUsername(username).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                var authResponse = AuthenticationResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            } else {
                throw new ApiException("Solicitud incorrecta");
            }
        } else {
            throw new ApiException("Solicitud incorrecta");
        }
    }
}
