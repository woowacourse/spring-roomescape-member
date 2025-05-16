package roomescape.user.dto.response;

import roomescape.user.domain.User;

public record UserSelectElementResponse(Long id, String name) {

    public static UserSelectElementResponse from(User user) {
        return new UserSelectElementResponse(user.getId(), user.getName());
    }
}
