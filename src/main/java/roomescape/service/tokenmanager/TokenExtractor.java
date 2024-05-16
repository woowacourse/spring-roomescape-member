package roomescape.service.tokenmanager;

import jakarta.servlet.http.Cookie;

public interface TokenExtractor {
    String extractByCookies(Cookie[] cookies);

    String extractMemberIdByToken(String token);
}
