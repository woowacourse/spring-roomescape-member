package roomescape.auth.jwt;

import roomescape.business.model.entity.User;
import roomescape.business.model.vo.AuthToken;
import roomescape.business.model.vo.LoginInfo;

public interface JwtUtil {

    AuthToken createToken(User user);

    LoginInfo resolveToken(String tokenValue);

    boolean validateToken(String tokenValue);
}
