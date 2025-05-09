package roomescape.intercept;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.MemberRole;
import roomescape.dto.other.AuthenticationInformation;
import roomescape.exception.BadRequestException;
import roomescape.exception.ForbiddenException;
import roomescape.utility.JwtTokenProvider;

public class NotAdminBlockIntercept implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public NotAdminBlockIntercept(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        boolean isAdminRequest = request.getRequestURI().startsWith("/admin");
        if (isAdminRequest) {
            String accessToken = getAccessTokenInCookie(request);
            AuthenticationInformation authenticationInformation = parseAccessToken(accessToken);
            boolean isAdmin = authenticationInformation.role().equals(MemberRole.ADMIN);
            if (!isAdmin) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new ForbiddenException("[ERROR] 접근권한이 존재하지 않습니다.");
            }
        }
        return true;
    }

    private String getAccessTokenInCookie(HttpServletRequest request) {
        List<Cookie> cookies = List.of();
        if (request.getCookies() != null) {
            cookies = List.of(request.getCookies());
        }
        Optional<Cookie> accessTokenCookie = cookies.stream()
                .filter(cookie -> cookie.getName().equals("access"))
                .findFirst();
        if (accessTokenCookie.isEmpty()) {
            throw new BadRequestException("[ERROR] 인증 정보가 존재하지 않습니다.");
        }
        return accessTokenCookie.get().getValue();
    }

    private AuthenticationInformation parseAccessToken(String accessToken) {
        Claims tokenContent = jwtTokenProvider.parseToken(accessToken);
        Long id = Long.valueOf(tokenContent.getSubject());
        String name = String.valueOf(tokenContent.get("name"));
        MemberRole role = MemberRole.valueOf(String.valueOf(tokenContent.get("role")));
        return new AuthenticationInformation(id, name, role);
    }
}
