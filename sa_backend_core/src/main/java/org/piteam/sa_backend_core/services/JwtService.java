package org.piteam.sa_backend_core.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${app.jwt.expiration}")
    private long expiration;

    public JwtService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(String email, String role) {
        Instant now = Instant.now();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build(); // ← add this

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("sa_backend_core")
                .issuedAt(now)
                .expiresAt(now.plus(48, ChronoUnit.HOURS))
                .subject(email)
                .claim("role", role)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue(); // ← pass header
    }

    public String extractEmail(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    public String extractRole(String token) {
        return jwtDecoder.decode(token).getClaim("role");
    }
}
