package roomescape.cofig;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.infra.JwtProvider;
import roomescape.exception.UnAuthorizedException;
import roomescape.member.Member;
import roomescape.member.Role;
import roomescape.member.dao.MemberDao;

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

        String email = jwtProvider.getEmail(token);
        Member member = memberDao.findMember(email)
                .orElseThrow(UnAuthorizedException::new);

        if (member.getRole() != Role.ADMIN) {
            throw new UnAuthorizedException();
        }

        return true;
    }
}
