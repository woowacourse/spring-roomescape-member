package roomescape.member.presentation;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthorizationExtractor {
    String extract(HttpServletRequest request);
}
