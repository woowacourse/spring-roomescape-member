package roomescape.dto.request;

import org.apache.tomcat.util.http.parser.Cookie;

public record LoginCheckRequest(Cookie cookie) { // TODO 안쓸것같은데?
}
