package roomescape.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.exception.ErrorResponse;
import roomescape.service.auth.exception.InvalidTokenException;
import roomescape.service.auth.exception.MemberNotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class VerifyAdminInterceptor implements HandlerInterceptor {

    private final AuthorizationExtractor<String> authorizationExtractor;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public VerifyAdminInterceptor(AuthService authService) {
        this.authorizationExtractor = new CookieAuthorizationExtractor();
        this.authService = authService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = authorizationExtractor.extract(request);

        Member member;
        try {
            member = authService.getMemberByToken(token);
        } catch (MemberNotFoundException | InvalidTokenException e) {
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "유효 하지 않은 토큰 입니다.");
            return false;
        } catch (Exception e) {
            setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "토큰 인증에 실패 하였습니다.");
            return false;
        }

        if (!member.getRole().equals(Role.ADMIN)) {
            setErrorResponse(response, HttpStatus.FORBIDDEN, "접속 권한이 없습니다.");
            return false;
        }

        return true;
    }

    private void setErrorResponse(HttpServletResponse response, HttpStatus httpStatus, String message) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.setStatus(httpStatus.value());

        try {
            String errorJson = objectMapper.writeValueAsString(new ErrorResponse(message));
            response.getWriter().write(errorJson);
        } catch (IOException ignored) {
        }
    }
}
