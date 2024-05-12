package roomescape.auth.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.AccessTokenCookie;
import roomescape.auth.dto.LoggedInMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.RequestCookies;
import roomescape.exception.JwtAuthenticationException;
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

    public AccessTokenCookie createAccessToken(LoginRequest request) {
        Member member = memberDao.findMemberByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보가 잘못 되었습니다."));
        String values = createTokenValue(member);
        return new AccessTokenCookie(values);
    }

    private String createTokenValue(Member member) {
        return Jwts.builder()
                .claims()
                .add("id", member.getId())
                .and()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public LoggedInMember findLoggedInMember(RequestCookies cookies) {
        String token = new AccessTokenCookie(cookies.toMap()).getValue();
        Long id = findMemberId(token);
        Member member = memberDao.findMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
        return LoggedInMember.from(member);
    }

    private Long findMemberId(String token) {
        try {
            return tryToFindMemberId(token);
        } catch (ExpiredJwtException exception) {
            throw new JwtAuthenticationException("토큰의 유효 기간이 만료되었습니다.");
        } catch (MalformedJwtException exception) {
            throw new JwtAuthenticationException("토큰의 형식이 잘못되었습니다.");
        } catch (InvalidClaimException exception) {
            throw new JwtAuthenticationException("필요한 정보를 포함하고 있지 않습니다.");
        } catch (JwtException exception) {
            throw new JwtAuthenticationException("해당 토큰은 잘못된 토큰입니다.");
        }
    }

    private Long tryToFindMemberId(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Long.class);
    }
}
