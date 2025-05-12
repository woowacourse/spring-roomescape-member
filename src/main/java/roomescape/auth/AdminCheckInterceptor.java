package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.UnAuthorizedException;
import roomescape.member.Member;
import roomescape.member.Role;
import roomescape.member.dao.MemberDao;
import roomescape.member.infra.JwtProvider;

public class AdminCheckInterceptor implements HandlerInterceptor {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public AdminCheckInterceptor(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new UnAuthorizedException();
        }

        String token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(UnAuthorizedException::new)
                .getValue();

        String payload = jwtProvider.getPayload(token);
        Member member = memberDao.findMember(payload)
                .orElseThrow(UnAuthorizedException::new);

        if (member.getRole() != Role.ADMIN) {
            throw new UnAuthorizedException();
        }

        return true;
    }
}
