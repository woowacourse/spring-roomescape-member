package roomescape.member.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.AccessToken;
import roomescape.member.dto.LoginRequest;

@Service
public class AuthService {
    private final MemberDao memberDao;
    @Value("${jwt.secret-key}")
    private String secretKey;

    public AuthService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public String makeToken(LoginRequest request) {
        Member member = memberDao.findMemberByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보가 잘못 되었습니다."));
        return Jwts.builder()
                .claims()
                .add("id", member.getId())
                .add("email", member.getEmail())
                .add("name", member.getName())
                .and()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public AccessToken decode(String token) {
        Long id = getPayload(token).get("id", Long.class);
        String email = getPayload(token).get("email", String.class);
        String name = getPayload(token).get("name", String.class);
        return new AccessToken(id, email, name);
    }

    public Member findMember(String token) {
        Long id = getPayload(token).get("id", Long.class);
        return memberDao.findMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
