package org.marketplace_lea.common.common.service.jwt.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.marketplace_lea.common.common.service.jwt.JwtTokenService;
import org.marketplace_lea.common.common.service.jwt.properties.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.dtos.CustomerTokenInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.marketplace_lea.common.common.utils.SecurityUtils.AUTHORIZATION_TOKEN_PREFIX;
import static java.util.Arrays.stream;

@Slf4j
@Service
public class DefaultJwtTokenService implements JwtTokenService {
    private final JwtProperties jwtProperties;
    private final JWSAlgorithm algorithm = JWSAlgorithm.HS256;

    public DefaultJwtTokenService(JwtProperties properties) {
        this.jwtProperties = properties;
    }

    /**
     * Génère un token JWT pour un utilisateur avec ses rôles.
     * @param data données qu'on a besoin.
     * @return token JWT sérialisé.
     */
    @Override
    public String generateToken(Map<String, Object> data) {
        try {
            Date now = new Date();
            Date expiration = new Date(now.getTime() + jwtProperties.getExpirationMs());

            String username = (String) data.getOrDefault("username", "Aucun login !");
            String countryCode = (String) data.getOrDefault("country-code", "");

            JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("roles",
                            data.getOrDefault("roles", Collections.emptyList()))
                    .claim("subscriptions",
                            data.getOrDefault("subscriptions", Collections.emptyList()))
                    .claim("country-code", countryCode)
                    .issueTime(now)
                    .expirationTime(expiration);

            if(data.containsKey("customer")) {
                claimsSetBuilder.claim("customer", data.get("customer"));
            }

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(algorithm), claimsSetBuilder.build());
            MACSigner signer = new MACSigner(jwtProperties.getSecret().getBytes());
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Error generating JWT token for user: {}", data.get("username"), e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }


    /**
     * Valide un token et vérifie sa signature.
     * @param token token JWT
     * @return true si valide, false sinon
     */
    @Override
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            MACVerifier verifier = new MACVerifier(jwtProperties.getSecret().getBytes());
            if (!signedJWT.verify(verifier)) {
                log.warn("Invalid JWT signature");
                return false;
            }
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expiration != null && expiration.after(new Date());
        } catch (ParseException | JOSEException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }


    /**
     * Extrait le nom d'utilisateur (subject) du token.
     */
    @Override
    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            log.error("Failed to parse token for username extraction", e);
            return null;
        }
    }


    /**
     * Extrait la liste des rôles du token.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Object rolesClaim = signedJWT.getJWTClaimsSet().getClaim("roles");
            if (rolesClaim instanceof List) {
                return (List<String>) rolesClaim;
            }
            return List.of();
        } catch (ParseException e) {
            log.error("Failed to parse token for roles extraction", e);
            return List.of();
        }
    }

    @Override
    public Map<String, Object> parseToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            MACVerifier verifier = new MACVerifier(jwtProperties.getSecret().getBytes());
            if (!signedJWT.verify(verifier)) {
                log.warn("Invalid JWT signature");
                return Map.of();
            }
            return signedJWT.getJWTClaimsSet().getClaims();
        } catch (ParseException | JOSEException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return Map.of();
        }
    }

    @Override
    public CustomerTokenInfo extractCustomerInfo(String token) {
        var extractedData = parseToken(token);
        return (CustomerTokenInfo) extractedData.getOrDefault("customer", null);
    }


    /**
     * Vérifie si le token est expiré.
     */
    @Override
    public boolean isTokenExpired(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expiration != null && expiration.before(new Date());
        } catch (ParseException e) {
            return true;
        }
    }

    @Override
    public UsernamePasswordAuthenticationToken extractAuthenticationToken(String header) {
        if (header == null) {
            return null;
        }

        try {
            String token = header.replace(String.format("%s ", AUTHORIZATION_TOKEN_PREFIX), "");

            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            /// On récupère les roles (Dans le JWT le roles n'est jamais null).
            String[] roles = claimsSet.getStringArrayClaim("roles");

            Collection<SimpleGrantedAuthority> authorities = stream(roles)
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            return new UsernamePasswordAuthenticationToken(claimsSet.getSubject(), null, authorities);
        } catch (ParseException e) {
            log.error("Failed to parse token for username extraction", e);
            return null;
        }
    }
}