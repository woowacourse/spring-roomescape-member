package roomescape.auth.token;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.dto.TokenResponse;

import java.util.Optional;

public interface TokenManager {

    Optional<String> extract(final HttpServletRequest request);

    void setToken(final HttpServletResponse response, final TokenResponse token);
}
