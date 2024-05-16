package roomescape.service;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import roomescape.domain.Role;
import roomescape.domain.exception.AuthFailException;
import roomescape.domain.infrastructure.JwtTokenProvider;
import roomescape.dto.MemberModel;
import roomescape.dto.response.TokenResponse;

@Service
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;

    public TokenService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(MemberModel member) {
        String accessToken = jwtTokenProvider.createToken(member);
        return new TokenResponse(accessToken);
    }

    public Long findTokenId(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        return jwtTokenProvider.findTokenSubject(token);
    }

    public Role findRole(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        String role = jwtTokenProvider.findTokenRole(token);
        return Role.valueOf(role);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthFailException("로그인 정보가 없습니다."));
    }
}
