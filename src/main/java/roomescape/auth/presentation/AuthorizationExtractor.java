package roomescape.auth.presentation;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthorizationExtractor<T> {

    T extract(HttpServletRequest request);
}
