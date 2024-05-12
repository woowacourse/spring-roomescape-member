package roomescape.service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.exception.member.AuthenticationFailureException;

public class JwtProvider {
    private static final String SECRET_KEY = "hellowootecoworldhihowareyouiamfinethankyouandyou";
    private static final String ROLE_CLAIM_KEY = "role";
    private static final String NAME_CLAIM_KEY = "name";

    private JwtProvider() {
        throw new AssertionError("유틸 클래스입니다. 생성할 수 없습니다.");
    }

    public static String encode(Member user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim(ROLE_CLAIM_KEY, user.getRoleAsString())
                .claim(NAME_CLAIM_KEY, user.getName())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public static Long decodeId(String token) {
        try {
            token = token.replace("token=", "");

            return Long.parseLong(Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject());
        } catch (Exception e) {
            throw new AuthenticationFailureException();
        }
    }


    public static Role decodeRole(String token) {
        try {
            token = token.replace("token=", "");

            return Role.of(Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(ROLE_CLAIM_KEY, String.class));
        } catch (Exception e) {
            throw new AuthenticationFailureException();
        }
    }

    public static String decodeName(String token) {
        try {
            token = token.replace("token=", "");

            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(NAME_CLAIM_KEY, String.class);
        } catch (Exception e) {
            throw new AuthenticationFailureException();
        }
    }

}
