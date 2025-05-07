package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestMatcher {

    boolean isMatch(HttpServletRequest request);
}
