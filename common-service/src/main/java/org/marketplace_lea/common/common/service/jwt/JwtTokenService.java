package org.marketplace_lea.common.common.service.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Map;

public interface JwtTokenService {
    String generateToken(Map<String, Object> data);

    boolean validateToken(String token);

    String extractUsername(String token);

    List<String> extractRoles(String token);

    boolean isTokenExpired(String token);

    UsernamePasswordAuthenticationToken extractAuthenticationToken(String header);
}
