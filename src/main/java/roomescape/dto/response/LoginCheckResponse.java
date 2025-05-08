package roomescape.dto.response;

import roomescape.domain.User;

public record LoginCheckResponse(
        String name
) {

    public static LoginCheckResponse from(User user) {
        return new LoginCheckResponse(user.name());
    }
}
