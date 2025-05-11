package roomescape.dto;

import roomescape.model.User;

public record LoginCheckResponse(
        String name
) {
    public static LoginCheckResponse from(User user) {
        return new LoginCheckResponse(user.getNameValue());
    }
}
