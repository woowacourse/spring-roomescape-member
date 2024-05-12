package roomescape.member.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.exception.MemberAuthenticationException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.LoggedInMember;
import roomescape.member.domain.Member;
import roomescape.member.dto.LoginRequest;

@Service
public class AuthService {
    private final MemberDao memberDao;
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Autowired
    public AuthService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public AuthService(MemberDao memberDao, String secretKey) {
        this.memberDao = memberDao;
        this.secretKey = secretKey;
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

    public LoggedInMember findLoggedInMember(String token) {
        Long id = getClaims(token).get("id", Long.class);
        Member member = memberDao.findMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
        return new LoggedInMember(member);
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException exception) {
            throw new MemberAuthenticationException();
        }
    }
}
