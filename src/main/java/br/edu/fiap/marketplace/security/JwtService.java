package br.edu.fiap.marketplace.security;

import br.edu.fiap.marketplace.dto.TokenResponse;
import br.edu.fiap.marketplace.entity.Usuario;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/** Emite JWT assinado usando a configuração criptográfica de JwtConfig. */
@Service
public class JwtService {
    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expirationSeconds;

    public JwtService(JwtEncoder jwtEncoder, @Value("${security.jwt.issuer}") String issuer,
                      @Value("${security.jwt.expiration-seconds}") long expirationSeconds) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
    }

    public TokenResponse gerarToken(Usuario usuario) {
        Instant emitidoEm = Instant.now();
        Instant expiraEm = emitidoEm.plusSeconds(expirationSeconds);
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer(issuer).issuedAt(emitidoEm)
                .expiresAt(expiraEm).subject(usuario.getEmail()).claim("usuarioId", usuario.getId()).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new TokenResponse(token, "Bearer", expirationSeconds, expiraEm);
    }
}
