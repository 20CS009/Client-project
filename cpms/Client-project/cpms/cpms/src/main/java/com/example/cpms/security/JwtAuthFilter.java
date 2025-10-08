package com.example.cpms.security;

import com.example.cpms.exception.JwtTokenException;
import com.example.cpms.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {
            // 🔹 Check if Authorization header exists and starts with Bearer
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new JwtTokenException("Missing or invalid Authorization header");
            }

            token = authHeader.substring(7);

            // 🔹 Extract username/email from token
            username = jwtUtil.extractEmail(token);

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            throw new JwtTokenException("JWT token has expired");
        } catch (JwtTokenException ex) {
            throw ex; // already our custom one
        } catch (Exception ex) {
            throw new JwtTokenException("Invalid JWT token");
        }

        // 🔹 Proceed only if username is valid and not yet authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
