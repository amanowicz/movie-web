package com.amanowicz.movieweb.config;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final String AUTH_HEADER = "Authorization";
    private final String AUTH_PREFIX = "Bearer ";
    private final String CLIENT_SECRET = "movieweb123";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isValidAuthHeader(request)) {
                Claims claims = getClaims(request);
                if (claims.get("authorities") != null){
                    setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    private void setUpSpringAuthentication(Claims claims) {
        List<String> authorities = (List<String>) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(),
                null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private Claims getClaims(HttpServletRequest request) {
        String jwtToken = request.getHeader(AUTH_HEADER).replace(AUTH_PREFIX, "");

        return Jwts.parser().setSigningKey(CLIENT_SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    private boolean isValidAuthHeader(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(AUTH_HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(AUTH_PREFIX);
    }
}
