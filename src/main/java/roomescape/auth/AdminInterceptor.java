package roomescape.auth;

import java.util.Objects;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import roomescape.domain.User;
import roomescape.exception.InvalidTokenException;
import roomescape.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        if (request == null || request.getCookies() == null) {
            throw new IllegalArgumentException();
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
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty() || !user.get().role().equals("admin")) {
                throw new AuthenticationException("일반 사용자는 접근할 수 없습니다.");
            }

            return true;
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException(e);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException("로그인 정보가 잘못되었습니다.");
        }
    }
}
