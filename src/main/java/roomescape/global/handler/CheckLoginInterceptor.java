package roomescape.global.handler;

import static roomescape.global.exception.ErrorMessage.ONLY_ACCESS_ADMIN;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.model.Role;
import roomescape.global.auth.JwtProvider;
import roomescape.global.exception.AuthorizedException;
import roomescape.global.utils.Cookies;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public CheckLoginInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        String token = Cookies.get(request.getCookies());
        Role memberRole = jwtProvider.getMemberRole(token);
        if (memberRole != Role.ADMIN) {
            throw new AuthorizedException(ONLY_ACCESS_ADMIN);
        }
        return true;
    }
}
