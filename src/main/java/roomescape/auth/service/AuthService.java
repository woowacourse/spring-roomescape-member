package roomescape.auth.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenWithCookieResponse;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.common.security.jwt.JwtTokenProvider;
import roomescape.common.util.CookieUtil;
import roomescape.user.service.UserService;

import java.util.Arrays;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public TokenWithCookieResponse createToken(TokenRequest tokenRequest) {
        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        ResponseCookie responseCookie = CookieUtil.createJwtCookie(accessToken);
        return new TokenWithCookieResponse(accessToken, responseCookie);
    }

    public LoginCheckResponse checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        String token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("jwt_token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("유효한 토큰이 존재하지 않습니다."));

        try {
            String email = jwtTokenProvider.getEmail(token);
            return LoginCheckResponse.from(userService.findNameByEmail(email));
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }
}
