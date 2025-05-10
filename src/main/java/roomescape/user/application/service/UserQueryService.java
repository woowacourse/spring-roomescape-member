package roomescape.user.application.service;

import roomescape.user.domain.User;

public interface UserQueryService {

    User getByEmail(String email);
}
