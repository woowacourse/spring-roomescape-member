package roomescape.user.application.service;

import roomescape.common.domain.Email;
import roomescape.user.application.dto.UserPublicInfoResponse;
import roomescape.user.domain.User;
import roomescape.user.domain.UserId;

public interface UserQueryService {

    User getByEmail(Email email);

    UserPublicInfoResponse getPubicInfoById(UserId id);
}
