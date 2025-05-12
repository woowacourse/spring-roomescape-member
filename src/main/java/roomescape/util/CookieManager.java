package roomescape.util;

import jakarta.servlet.http.Cookie;

public class CookieManager {

    public static String extractTokenFromCookies(Cookie[] cookies) {
        if(cookies==null||cookies.length==0){
            return "";
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
