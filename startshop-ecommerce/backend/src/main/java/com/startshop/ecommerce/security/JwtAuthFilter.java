package com.startshop.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Este filtro se ejecuta en CADA peticion HTTP, ANTES de llegar al controller.
 * Su trabajo: revisar si viene un header "Authorization: Bearer <token>",
 * validar el token, y si es valido, decirle a Spring Security "este usuario
 * ya esta autenticado" para que pueda entrar a endpoints protegidos.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        final String email;

        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            // Token invalido o mal formado: dejamos pasar la peticion sin autenticar.
            // Sera el propio endpoint protegido el que responda 401/403.
            filterChain.doFilter(request, response);
            return;
        }

        boolean noHayAutenticacionAun = SecurityContextHolder.getContext().getAuthentication() == null;

        if (email != null && noHayAutenticacionAun) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtUtil.isTokenValid(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
