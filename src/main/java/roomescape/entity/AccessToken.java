package roomescape.entity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AccessToken {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final String value;

    public AccessToken(Member member) {
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        this.value = accessToken;
    }

    public String getValue() {
        return value;
    }
}
