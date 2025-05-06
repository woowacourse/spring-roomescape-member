package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.security.Keys;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.exception.auth.AuthenticationException;

public class JwtTokenProvider {

    private static final String COOKIE_KEY = "token";
    private static final String USER_NAME_KEY = "username";
    private static final String NAME_KEY = "name";
    private static final String ROLE_KEY = "role";

    private final String secretKey;


    public JwtTokenProvider(String secretKey) {
        this.secretKey = secretKey;
    }

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim(USER_NAME_KEY, member.getUsername())
                .claim(NAME_KEY, member.getName())
                .claim(ROLE_KEY, member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public static String getCookieKey() {
        return COOKIE_KEY;
    }

    public UserInfo resolveToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long id = Long.parseLong(claims.getSubject());
            String username = (String) claims.get("username");
            String name = (String) claims.get("name");
            Role role = Role.valueOf((String) claims.get("role"));
            return new UserInfo(id, username, name, role);
        } catch (RequiredTypeException e) {
            throw new AuthenticationException("토큰 파싱에 실패하였습니다");
        }
    }
}
