package roomescape.common.authorization;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthorizationHandler<T> {
    String AUTHORIZATION = "token";

    T extractToken(HttpServletRequest request);

    void createCookie(String tokenRequestContent, HttpServletResponse response);

    void deleteCookie(HttpServletRequest request, HttpServletResponse response);
}
