package roomescape.entity;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import roomescape.exception.InvalidAccessTokenException;

public class AccessToken {
    private static final long EXPIRE_LENGTH = 3600000;
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final String value;

    public AccessToken(String value) {
        this.value = value;
    }

    public AccessToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRE_LENGTH);

        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("role", member.getRole().name())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        this.value = accessToken;
    }

    public String getValue() {
        return value;
    }

    public long findSubject() {
        try {
            return Long.parseLong(Jwts.parser().setSigningKey(SECRET_KEY)
                    .parseClaimsJws(value)
                    .getBody()
                    .getSubject());
        } catch (NumberFormatException | JwtException e) {
            //TODO 파싱 자체에 실패한 경우
            throw new InvalidAccessTokenException();
        }
    }

    public MemberRole findRole() {
        try {
            String roleName = Jwts.parser().setSigningKey(SECRET_KEY)
                    .parseClaimsJws(value)
                    .getBody()
                    .get("role", String.class);
            return MemberRole.from(roleName);
        } catch (NumberFormatException | JwtException e) {
            //TODO 파싱 자체에 실패한 경우
            throw new InvalidAccessTokenException();
        }
    }
}
