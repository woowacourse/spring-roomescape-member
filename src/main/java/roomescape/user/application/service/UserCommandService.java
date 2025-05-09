package roomescape.user.application.service;

import roomescape.user.application.dto.CreateUserServiceRequest;
import roomescape.user.domain.User;

public interface UserCommandService {

    User create(CreateUserServiceRequest request);
}
