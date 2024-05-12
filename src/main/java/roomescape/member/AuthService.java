package roomescape.member;


import jakarta.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.member.request.LoginRequest;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public boolean checkInvalidLogin(String email, String password) {
        return !memberDao.isMember(email, password);
    }

    public String createToken(LoginRequest loginRequest) {
        if (checkInvalidLogin(loginRequest.email(), loginRequest.password())) {
            throw new IllegalArgumentException("login failed");
        }
        Member member = memberDao.findMemberByEmail(loginRequest.email());
        return jwtTokenProvider.createToken(member);
    }

    public long findMemberIdByToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
