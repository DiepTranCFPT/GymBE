package com.gymsystem.cyber.config;

import com.gymsystem.cyber.service.TokenService;
import com.gymsystem.cyber.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final JwtDecoder jwtDecoder;
    private final UserService userService;

    public JwtAuthenticationFilter(TokenService tokenService, JwtDecoder jwtDecoder, UserService userService) {
        this.tokenService = tokenService;
        this.jwtDecoder = jwtDecoder;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        logger.debug("Received token: {}");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                Jwt jwt = jwtDecoder.decode(token);
                String email = jwt.getClaim("email");

                UserDetails userDetails = userService.loadUserByUsername(email);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (JwtException e) {
                logger.error("JWT Authentication failed", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
