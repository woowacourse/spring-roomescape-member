package roomescape.user.application;

import roomescape.user.ui.dto.UserWebResponse;
import roomescape.user.ui.dto.CreateUserWebRequest;

public interface UserWebFacade {

    UserWebResponse get();

    UserWebResponse create(CreateUserWebRequest request);
}
