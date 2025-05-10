package roomescape.infrastructure.intercepter;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestMatcher {

    boolean isMatch(HttpServletRequest request);
}
