package roomescape.domain.auth.dto;

import roomescape.domain.auth.entity.User;

public record UserInfoResponse(Long id, String name) {

    public static UserInfoResponse from(final User user) {
        return new UserInfoResponse(user.getId(), user.getName());
    }
}
