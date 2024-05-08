package roomescape.dto.request;

import org.apache.tomcat.util.http.parser.Cookie;

public record LoginCheckRequest(Cookie cookie) {
}
