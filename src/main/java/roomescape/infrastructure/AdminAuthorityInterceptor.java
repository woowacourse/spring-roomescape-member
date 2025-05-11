package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.business.Member;
import roomescape.business.service.AuthenticationService;
import roomescape.exception.MemberException;

@Component
public class AdminAuthorityInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    @Autowired
    public AdminAuthorityInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            // todo: 401 exception & 중복 코드 제거
            throw new MemberException("토큰이 존재하지 않습니다.");
        }
        // todo: 401 exception
        String token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new MemberException("토큰이 존재하지 않습니다."))
                .getValue();
        Member member = authenticationService.findMemberByToken(token);
        return member.isAdmin();
    }
}
