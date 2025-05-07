package roomescape.entity;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import roomescape.exception.InvalidAccessTokenException;

public class AccessToken {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final String value;

    public AccessToken(String value) {
        this.value = value;
    }

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

    public long findSubject() {
        try {
            return Long.parseLong(Jwts.parser().setSigningKey(SECRET_KEY)
                    .parseClaimsJws(value)
                    .getBody()
                    .getSubject());
        } catch (NumberFormatException | JwtException e) {
            //TODO : 이 에러를 잡는 이유를 이해할까? 예외처리가 진짜 예외처리가 맞나?
            //TODO 파싱 자체에 실패한 경우
            throw new InvalidAccessTokenException();
        }
    }
}
