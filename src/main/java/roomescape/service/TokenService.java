package roomescape.service;

import jakarta.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import roomescape.domain.Role;
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
        return jwtTokenProvider.findTokenSubject(token);
    }

    public Role findRole(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        String role = jwtTokenProvider.findTokenRole(token);
        return Role.valueOf(role);
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
