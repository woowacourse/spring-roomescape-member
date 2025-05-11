package roomescape.user.application.service;

import roomescape.common.domain.Email;
import roomescape.user.domain.User;

public interface UserQueryService {

    User getByEmail(Email email);
}
