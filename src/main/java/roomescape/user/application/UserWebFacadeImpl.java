package roomescape.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.user.application.service.UserCommandService;
import roomescape.user.application.service.UserQueryService;
import roomescape.user.domain.User;
import roomescape.user.ui.dto.CreateUserWebRequest;
import roomescape.user.ui.dto.UserWebResponse;

@Service
@RequiredArgsConstructor
public class UserWebFacadeImpl implements UserWebFacade {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Override
    public UserWebResponse get() {
        // TODO not yet
        return null;
    }

    @Override
    public UserWebResponse create(final CreateUserWebRequest request) {
        final User user = userCommandService.create(request.toServiceRequest());
        return new UserWebResponse(
                user.getId().getValue(),
                user.getName(),
                user.getEmail(),
                user.getPassword());
    }
}
