package roomescape.auth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import roomescape.exception.ErrorCode;

@Component
public class AuthExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
        if (ex instanceof AuthorizationException ae) {
            ErrorCode code = ae.getErrorCode();

            ProblemDetail problem = ProblemDetail.forStatus(code.getStatus());
            problem.setTitle("인증 오류");
            problem.setDetail(code.getMessage());
            problem.setProperty("code", code.name());
            problem.setInstance(URI.create(request.getRequestURI()));

            response.setStatus(code.getStatus().value());
            response.setContentType("application/json;charset=UTF-8");

            try {
                response.getWriter().write(new ObjectMapper().writeValueAsString(problem));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new ModelAndView();
        }

        return null;
    }
}
