package roomescape.service;

import jakarta.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import roomescape.dto.MemberModel;
import roomescape.dto.response.TokenResponse;
import roomescape.infrastructure.JwtTokenProvider;

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
        return jwtTokenProvider.findMember(token);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
