package roomescape.domain.auth.dto;

import roomescape.domain.auth.entity.User;

public record UserInfoResponse(String name) {

    public static UserInfoResponse from(final User user) {
        return new UserInfoResponse(user.getName());
    }
}
