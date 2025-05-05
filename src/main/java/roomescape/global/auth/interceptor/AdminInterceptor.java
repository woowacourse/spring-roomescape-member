package roomescape.global.auth.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.auth.annotation.Admin;
import roomescape.global.auth.jwt.JwtHandler;
import roomescape.global.exception.impl.ForbiddenException;
import roomescape.global.exception.impl.NotFoundException;
import roomescape.global.exception.impl.UnauthorizedException;
import roomescape.member.business.model.entity.Member;
import roomescape.member.business.model.entity.Role;
import roomescape.member.business.model.repository.MemberDao;


@Component
public class AdminInterceptor implements HandlerInterceptor {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

    private final MemberDao memberDao;
    private final JwtHandler jwtHandler;

    public AdminInterceptor(final MemberDao memberDao, final JwtHandler jwtHandler) {
        this.memberDao = memberDao;
        this.jwtHandler = jwtHandler;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Admin adminAnnotation = handlerMethod.getMethodAnnotation(Admin.class);
        if (adminAnnotation == null) {
            return true;
        }

        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(ACCESS_TOKEN_COOKIE_NAME)) {
                    String accessToken = cookie.getValue();
                    Long memberId = jwtHandler.getMemberIdFromToken(accessToken);

                    Member member = memberDao.findById(memberId)
                            .orElseThrow(() -> new NotFoundException("해당하는 사용자가 없습니다."));
                    return checkRole(member);
                }
            }
        }
        throw new UnauthorizedException("JWT 토큰이 존재하지 않거나 유효하지 않습니다.");
    }

    private boolean checkRole(final Member member) {
        if (member.isRole(Role.ADMIN)) {
            return true;
        }
        throw new ForbiddenException("회원 권한이 존재하지 않아 접근할 수 없습니다.");
    }

}
