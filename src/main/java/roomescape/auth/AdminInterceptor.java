package roomescape.auth;

import java.util.Objects;

import javax.naming.AuthenticationException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import roomescape.domain.User;
import roomescape.service.UserService;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        if (request == null || request.getCookies() == null) {
            // TODO: 예외처리
            throw new AuthenticationException();
        }
        String token = null;
        for (var cookie : request.getCookies()) {
            if (Objects.equals(cookie.getName(), "token")) {
                token = cookie.getValue();
                break;
            }
        }

        try {
            Long userId = Long.valueOf(jwtProvider.getPayload(token));
            User user = userService.getById(userId);
            if (user == null || !user.role().equals("admin")) {
                response.setStatus(401);
                // TODO: 예외 구체화
                throw new AuthenticationException("일반 사용자는 접근할 수 없습니다.");
            }

            return true;
        } catch (ExpiredJwtException e) {
            // TODO: 예외 구체화 ; 핸들러에서 쿠키 제거
            throw new RuntimeException("로그아웃하세요");
        } catch (IllegalArgumentException e) {
            // TODO: 예외 구체화 고민
            throw new AuthenticationException("로그인 정보가 잘못되었습니다.");
        }
    }
}
