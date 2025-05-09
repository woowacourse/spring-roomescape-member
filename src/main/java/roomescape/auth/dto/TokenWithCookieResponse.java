package roomescape.auth.dto;

import org.springframework.http.ResponseCookie;

public class TokenWithCookieResponse {

    private final String accessToken;
    private final ResponseCookie responseCookie;

    public TokenWithCookieResponse(String accessToken, ResponseCookie responseCookie) {
        this.accessToken = accessToken;
        this.responseCookie = responseCookie;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public ResponseCookie getCookie() {
        return responseCookie;
    }
}
