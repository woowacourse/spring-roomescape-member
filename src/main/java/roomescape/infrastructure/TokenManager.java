package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenManager {

    String getToken(HttpServletRequest request);
    void setToken(String token, HttpServletResponse response);
    void expireToken(HttpServletResponse response);
}
