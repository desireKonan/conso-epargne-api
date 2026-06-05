package org.marketplace_lea.application.configuration.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.common.exceptions.ApplicationError;
import org.marketplace_lea.common.common.exceptions.ExceptionUserDisplay;
import org.marketplace_lea.common.common.service.jwt.JwtTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.marketplace_lea.common.common.utils.SecurityUtils.AUTHORIZATION_HEADER_KEY;
import static org.marketplace_lea.common.common.utils.SecurityUtils.AUTHORIZATION_TOKEN_PREFIX;


@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;
    private final ObjectMapper objectMapper;

    private static final String ERROR_MSG = "{\"message\":\"Vous n'êtes pas autorisé à accéder à cette ressource !\"}";

    public JwtAuthorizationFilter(JwtTokenService jwtTokenService, ObjectMapper objectMapper) {
        this.jwtTokenService = jwtTokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION_HEADER_KEY);
        if (header == null || !header.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
            log.info("Header not found");
            sendUnauthorizedResponse(response);
            return;
        }

        try {
            var authentication = jwtTokenService.extractAuthenticationToken(header);
            log.info("Parse Authentication Token: {}", authentication);
            if(authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            var error = buildError(e);
            log.error("Error while parsing authentication token: {}", error);
            sendUnauthorizedResponse(response, error);
        }
        filterChain.doFilter(request, response);
    }


    private void sendUnauthorizedResponse(HttpServletResponse response, ApplicationError error) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(ERROR_MSG);
    }

    private ApplicationError buildError(Exception e) {
        return new ApplicationError(
                HttpStatus.UNAUTHORIZED.name(),
                e.getLocalizedMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                ExceptionUserDisplay.ERROR
        );
    }
}
