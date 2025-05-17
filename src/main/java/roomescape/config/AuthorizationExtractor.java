package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface AuthorizationExtractor {

    Optional<String> extract(HttpServletRequest request);
}
