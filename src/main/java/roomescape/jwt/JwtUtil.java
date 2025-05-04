package roomescape.jwt;

import roomescape.business.model.entity.User;
import roomescape.business.model.vo.Authentication;

public interface JwtUtil {

    Authentication getAuthentication(User user);
}
