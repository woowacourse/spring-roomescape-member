package roomescape.member;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.member.request.LoginRequest;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public boolean checkInvalidLogin(String email, String password) {
        return !memberDao.existsByEmailAndPassword(email, password);
    }

    public String createToken(LoginRequest loginRequest) {
        if (checkInvalidLogin(loginRequest.email(), loginRequest.password())) {
            throw new IllegalArgumentException("login failed");
        }
        Member member = memberDao.findMemberByEmail(loginRequest.email())
                .orElseThrow(() -> new IllegalArgumentException(loginRequest.email() + "에 맞는 회원은 존재하지 않습니다."));
        return jwtTokenProvider.createToken(member);
    }

    public long findMemberIdByToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }

    public String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new IllegalStateException("토큰이 유효하지 않습니다.");
    }
}
