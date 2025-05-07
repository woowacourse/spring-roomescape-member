package roomescape.auth.jwt;

import roomescape.business.model.entity.User;
import roomescape.business.model.vo.Authentication;
import roomescape.business.model.vo.LoginInfo;

public interface JwtUtil {

    Authentication getAuthentication(User user);

    LoginInfo getAuthorization(String tokenValue);

    boolean validateToken(String tokenValue);
}
