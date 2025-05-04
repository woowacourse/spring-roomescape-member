package roomescape.jwt;

import roomescape.business.model.entity.User;
import roomescape.business.model.vo.Authentication;

// TODO : 외부 라이브러리를 사용하는 구현체 작성
public interface JwtUtil {

    Authentication getAuthentication(String secret, User user);
}
