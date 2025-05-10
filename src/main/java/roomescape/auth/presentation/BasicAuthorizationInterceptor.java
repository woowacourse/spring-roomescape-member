package roomescape.auth.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.application.AuthService;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.auth.exception.AuthorizationException;
import roomescape.auth.infrastructure.CookieAuthorizationExtractor;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Member;

@Component
public class BasicAuthorizationInterceptor implements HandlerInterceptor {

    private final CookieAuthorizationExtractor extractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    public BasicAuthorizationInterceptor(CookieAuthorizationExtractor extractor,
                                         JwtTokenProvider jwtTokenProvider,
                                         AuthService authService) {
        this.extractor = extractor;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Optional<String> result = extractor.extract(request);
        if (result.isEmpty()) {
            throw new AuthorizationException(AuthErrorCode.LOGIN_REQUIRED);
        }

        String token = result.get();
        validateToken(token);

        Member member = authService.findMemberByToken(token);
        request.setAttribute("loginMember", new LoginMember(member.getId(), member.getName()));
        return true;
    }

    private void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException(AuthErrorCode.INVALID_TOKEN);
        }
    }
}
