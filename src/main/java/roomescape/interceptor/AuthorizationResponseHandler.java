package roomescape.interceptor;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthorizationResponseHandler {

    static boolean redirectLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login");
        return false;
    }

    static boolean responseUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("권한이 없습니다.");
        writer.flush();
        return false;
    }
}
