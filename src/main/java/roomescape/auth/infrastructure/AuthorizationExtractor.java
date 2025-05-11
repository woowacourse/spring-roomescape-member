package roomescape.auth.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import roomescape.auth.exception.AuthException;

@Component
public class AuthorizationExtractor {

    private static final String COOKIE = "Cookie";
    private static final String AUTHORIZATION_KEY = "token=";

    public String extract(final NativeWebRequest webRequest) {
        String cookie = webRequest.getHeader(COOKIE);
        if (cookie == null || !cookie.toLowerCase().startsWith(AUTHORIZATION_KEY.toLowerCase())) {
            throw new AuthException("로그인이 필요합니다.");
        }
        String authHeaderValue = cookie.substring(AUTHORIZATION_KEY.length()).trim();
        System.out.println(authHeaderValue);
        int semicolonIndex = authHeaderValue.indexOf(";");
        if (semicolonIndex > 0) {
            authHeaderValue = authHeaderValue.substring(0, semicolonIndex);
        }
        return authHeaderValue;
    }
}
