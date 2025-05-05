package roomescape.auth.jwt;

import roomescape.business.model.entity.User;
import roomescape.business.model.vo.Authentication;
import roomescape.business.model.vo.Authorization;

public interface JwtUtil {

    Authentication getAuthentication(User user);

    Authorization getAuthorization(String tokenValue);

    boolean validateToken(String tokenValue);
}
