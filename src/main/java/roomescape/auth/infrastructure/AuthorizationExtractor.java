package roomescape.auth.infrastructure;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthorizationExtractor<T> {
    T extract(HttpServletRequest request);
}
