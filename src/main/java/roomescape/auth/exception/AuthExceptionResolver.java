package roomescape.auth.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import roomescape.global.exception.ErrorCode;

@Component
public class AuthExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
        if (!(ex instanceof AuthorizationException ae)) {
            return null;
        }

        ErrorCode code = ae.getErrorCode();

        try {
            handleAuthorizationError(response, code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ModelAndView();
    }

    private void handleAuthorizationError(HttpServletResponse response, ErrorCode code) throws IOException {
        if (code == AuthErrorCode.FORBIDDEN_ACCESS) {
            response.sendRedirect("/error/403.html");
        }
        redirectWithAlert(response, code.getMessage(), "/login");
    }

    private void redirectWithAlert(HttpServletResponse response, String message, String redirectUrl)
            throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(
                "<script>alert('" + message + "'); location.href='" + redirectUrl + "';</script>"
        );
    }
}
