package roomescape.entity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import roomescape.exception.InvalidTokenException;

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
                .claim("name", member.getName())
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

    public boolean isVerified() {
        try {
            getTokenBody();
            return true;
        } catch (InvalidTokenException e) {
            return false;
        }
    }

    public long findSubject() {
        String subject = getTokenBody().getSubject();
        return Long.parseLong(subject);
    }

    public String findMemberName() {
        return getTokenBody()
                .get("name", String.class);
    }

    public MemberRole findMemberRole() {
        String roleName = getTokenBody()
                .get("role", String.class);
        return MemberRole.from(roleName);
    }

    private Claims getTokenBody() {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY)
                    .parseClaimsJws(value)
                    .getBody();
        } catch (NumberFormatException | JwtException e) {
            throw new InvalidTokenException();
        }
    }
}
