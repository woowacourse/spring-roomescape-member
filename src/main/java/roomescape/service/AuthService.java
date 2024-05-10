package roomescape.service;

import jakarta.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import roomescape.model.User;

@Service
public class AuthService {

    private final TokenProvider tokenProvider;

    public AuthService(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public Cookie createCookieByUser(User user) {
        String jwt = tokenProvider.createToken(user);
        Cookie cookie = new Cookie("token", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
