package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.dto.TokenResponse;
import roomescape.infra.JwtTokenProcessor;
import roomescape.model.user.User;
import roomescape.model.user.UserName;
import roomescape.repository.LoginRepository;

@Service
public class LoginService {
    private final LoginRepository loginRepository;
    private final JwtTokenProcessor jwtTokenProcessor;

    public LoginService(LoginRepository loginRepository, JwtTokenProcessor jwtTokenProcessor) {
        this.loginRepository = loginRepository;
        this.jwtTokenProcessor = jwtTokenProcessor;
    }

    public User login(String email, String password) {
        User user = loginRepository.login(email, password);
        System.out.println(user);
        return user;
    }

    public TokenResponse createToken(String payload) {
        String accessToken = jwtTokenProcessor.createToken(payload);
        return new TokenResponse(accessToken);
    }

    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public String extractUserEmailFromCookies(Cookie[] cookies) {
        return jwtTokenProcessor.extractUserEmailFromCookie(cookies);
    }

    public UserName getUserNameByUserEmail(String userEmail) {
        return loginRepository.findUserNameByUserEmail(userEmail);
    }
}
