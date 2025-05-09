package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.LoginMember;
import roomescape.exception.InvalidAuthorizationException;
import roomescape.service.LoginMemberService;
import roomescape.util.CookieTokenExtractor;
import roomescape.util.JwtTokenProvider;

public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    private final LoginMemberService loginMemberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieTokenExtractor authorizationExtractor;

    public LoginInterceptor(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authorizationExtractor = new CookieTokenExtractor();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = authorizationExtractor.extract(request);
        if (token == null || token.isBlank()) {
            throw new InvalidAuthorizationException("[ERROR] 로그인이 필요합니다.");
        }

        jwtTokenProvider.validateToken(token);
        LoginMember member = loginMemberService.findMemberByToken(token);

        if (!member.getRole().equalsIgnoreCase("ADMIN")) {
            response.setStatus(403);
            return false;
        }
        return true;
    }
}
