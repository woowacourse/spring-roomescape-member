package roomescape.presentation.dto.response;

import roomescape.business.model.entity.User;

public record UserNameResponse(
        String name
) {
    public static UserNameResponse from(final User user) {
        return new UserNameResponse(user.name());
    }
}
