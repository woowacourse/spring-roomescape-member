package roomescape.auth.infrastructure;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.global.exception.error.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;

@Component
public class JwtTokenProvider {

    private static final String NAME_CLAIM_KEY = "name";
    private static final String EMAIL_CLAIM_KEY = "email";
    private static final String ROLE_CLAIM_KEY = "role";

    private final Algorithm algorithm;
    private final Long expirationInMilliseconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret.key}") String secretKey,
            @Value("${security.jwt.token.expiration}") Long expirationInMilliseconds

    ) {
        algorithm = Algorithm.HMAC256(secretKey);
        this.expirationInMilliseconds = expirationInMilliseconds;
    }

    public String createToken(Member member) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationInMilliseconds);

        return JWT.create()
                .withSubject(member.getId().toString())
                .withClaim(NAME_CLAIM_KEY, member.getName())
                .withClaim(EMAIL_CLAIM_KEY, member.getEmail())
                .withClaim(ROLE_CLAIM_KEY, member.getRole().toString())
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .sign(algorithm);
    }

    public JwtPayload getPayload(String token) {
        DecodedJWT decodedJWT = verifyAndDecodeToken(token);
        Map<String, Claim> claims = decodedJWT.getClaims();

        return new JwtPayload(
                Long.parseLong(decodedJWT.getSubject()),
                getRequiredClaim(claims, NAME_CLAIM_KEY, Claim::asString),
                getRequiredClaim(claims, EMAIL_CLAIM_KEY, Claim::asString),
                getRequiredClaim(claims, ROLE_CLAIM_KEY, claim -> Role.valueOf(claim.asString()))
        );
    }

    public void validateToken(String token) {
        verifyAndDecodeToken(token);
    }

    private DecodedJWT verifyAndDecodeToken(String token) {
        try {
            return JWT.require(algorithm)
                    .build()
                    .verify(token);
        } catch (TokenExpiredException e) {
            throw new UnauthorizedException("토큰이 만료되었습니다.");
        } catch (SignatureVerificationException e) {
            throw new UnauthorizedException("토큰 내부 서명이 올바르지 않습니다.");
        } catch (JWTVerificationException e) {
            throw new UnauthorizedException("JWT 검증에 실패했습니다.");
        }
    }

    private <T> T getRequiredClaim(Map<String, Claim> claims, String key, Function<Claim, T> extractor) {
        Claim claim = claims.get(key);
        if (claim == null || claim.isNull()) {
            throw new UnauthorizedException("JWT에 [" + key + "] 클레임이 없습니다.");
        }
        return extractor.apply(claim);
    }

}
