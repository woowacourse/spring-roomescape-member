package roomescape.infrastructure.intercepter;


import jakarta.servlet.http.HttpServletRequest;

public class AdminRequestMapper implements RequestMatcher {

    private static final String ADMIN_START_URI = "/admin";

    public boolean isMatch(HttpServletRequest request) {
        return request.getRequestURI().startsWith(ADMIN_START_URI);
    }
}
