package com.startshop.ecommerce.service;

import com.startshop.ecommerce.dto.AuthResponse;
import com.startshop.ecommerce.dto.LoginRequest;
import com.startshop.ecommerce.dto.RegisterRequest;
import com.startshop.ecommerce.entity.Role;
import com.startshop.ecommerce.entity.User;
import com.startshop.ecommerce.exception.BadRequestException;
import com.startshop.ecommerce.repository.UserRepository;
import com.startshop.ecommerce.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil,
                        AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Ya existe una cuenta con ese email");
        }

        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password()), // NUNCA guardamos el password tal cual
                request.fullName(),
                Role.CLIENTE
        );

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getFullName(), user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        // Esto valida el email + password contra la BD (usa UserDetailsServiceImpl + PasswordEncoder).
        // Si falla, lanza BadCredentialsException automaticamente.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getFullName(), user.getEmail(), user.getRole().name());
    }
}
