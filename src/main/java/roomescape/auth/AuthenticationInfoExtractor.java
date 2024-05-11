package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import roomescape.auth.exception.AuthorizationException;

@Component
public class AuthenticationInfoExtractor {
    private static final String AUTHENTICATION_INFO_KEY_NAME = "token";

    private final TokenProvider tokenProvider;

    public AuthenticationInfoExtractor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public long extractMemberId(HttpServletRequest request) {
        Cookie authenticationInfo = WebUtils.getCookie(request, AUTHENTICATION_INFO_KEY_NAME);
        if (authenticationInfo == null) {
            throw new AuthorizationException("인증 정보가 없습니다.");
        }
        String token = authenticationInfo.getValue();
        String subject = tokenProvider.extractSubject(token);
        return Long.parseLong(subject);
    }
}
