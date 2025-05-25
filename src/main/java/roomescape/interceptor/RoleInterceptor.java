package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.LoginMember;
import roomescape.domain.Role;
import roomescape.exception.InvalidCredentialsException;
import roomescape.exception.UnauthorizedAccessException;
import roomescape.service.MemberService;
import roomescape.util.CookieExtractor;
import roomescape.util.CookieKeys;
import roomescape.util.JwtTokenProvider;

public class RoleInterceptor implements HandlerInterceptor {

    private static final Map<String, List<String>> ADMIN_ACCESSIBLE_METHODS = Map.of(
            "times", List.of("POST", "DELETE"),
            "themes", List.of("POST", "DELETE"),
            "members", List.of("GET"),
            "reservations", List.of("GET", "DELETE"));
    private static final Map<String, List<String>> GUEST_ACCESSIBLE_METHODS = Map.of(
            "members", List.of("POST"),
            "times", List.of("GET"),
            "themes", List.of("GET"));

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieExtractor authorizationExtractor;

    public RoleInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authorizationExtractor = new CookieExtractor();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String baseUri = getBaseUri(request.getRequestURI());
        String requestMethod = request.getMethod();

        List<String> guestMethods = GUEST_ACCESSIBLE_METHODS.get(baseUri);
        if (GUEST_ACCESSIBLE_METHODS.containsKey(baseUri) && guestMethods.contains(requestMethod)) {
            return true;
        }

        String token = authorizationExtractor.extract(request, CookieKeys.TOKEN);
        if (token == null || token.isBlank()) {
            throw new InvalidCredentialsException("[ERROR] 로그인이 필요합니다.");
        }

        jwtTokenProvider.validateToken(token);
        LoginMember member = memberService.findMemberByToken(token);
        List<String> adminMethods = ADMIN_ACCESSIBLE_METHODS.get(baseUri);

        if (!adminMethods.contains(requestMethod) ||
                adminMethods.contains(requestMethod) && member.getRole() == Role.ADMIN) {
            return true;
        }
        throw new UnauthorizedAccessException("[ERROR] 접근 권한이 없습니다.");
    }

    private String getBaseUri(String requestUri) {
        return requestUri.substring(1).split("/")[0];
    }
}
