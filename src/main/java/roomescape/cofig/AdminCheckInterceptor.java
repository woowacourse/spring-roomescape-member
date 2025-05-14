package roomescape.cofig;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.infra.JwtProvider;
import roomescape.exception.UnAuthorizedException;
import roomescape.member.Member;
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
            throw new UnAuthorizedException("인증 정보가 포함된 쿠키가 없습니다.");
        }

        String token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new UnAuthorizedException("토큰이 쿠키에 존재하지 않습니다."))
                .getValue();

        String email = jwtProvider.getEmail(token);
        Member member = memberDao.findMember(email)
                .orElseThrow(() -> new UnAuthorizedException("해당 토큰에 해당하는 사용자를 찾을 수 없습니다."));

        member.validateMemberRole();
        return true;
    }
}
