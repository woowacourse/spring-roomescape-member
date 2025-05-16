package roomescape.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.custom.AuthorizationException;

@Component
public class JwtTokenExtractor implements AuthTokenExtractor {

    private final JWTVerifier verifier;

    public JwtTokenExtractor(@Value("${security.jwt.token.secret-key}") String secretKey) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        this.verifier = JWT.require(algorithm).build();
    }

    @Override
    public String extractMemberIdFromToken(String token) {
        validateValidToken(token);
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }

    @Override
    public String extractMemberRoleFromToken(String token) {
        validateValidToken(token);
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("role").asString();
    }

    private void validateValidToken(final String token) {
        validateTokenExists(token);
        validateTokenIntegrityAndExpiration(token);
    }

    private void validateTokenExists(final String token) {
        if (token == null || token.isBlank()) {
            throw new AuthorizationException("로그인 토큰이 존재하지 않습니다");
        }
    }

    private void validateTokenIntegrityAndExpiration(String token) {
        try {
            verifier.verify(token);
        } catch (TokenExpiredException e) {
            throw new AuthorizationException("토큰이 만료 되었습니다");
        } catch (JWTVerificationException | IllegalArgumentException e) {
            throw new AuthorizationException("서명이 올바르지 않거나 잘못된 토큰입니다");
        }
    }
}
