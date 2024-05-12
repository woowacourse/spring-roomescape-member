package roomescape.auth.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoggedInMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.exception.MemberAuthenticationException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;

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
                .and()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public LoggedInMember findLoggedInMember(String token) {
        Long id = getMemberId(token);
        Member member = memberDao.findMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
        return LoggedInMember.from(member);
    }

    private Long getMemberId(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("id", Long.class);
        } catch (JwtException exception) {
            throw new MemberAuthenticationException();
        }
    }
}
