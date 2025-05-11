package roomescape.user.dto.response;

import roomescape.user.domain.User;

public record BriefUserResponse(Long id, String name) {

    public static BriefUserResponse from(User user) {
        return new BriefUserResponse(user.getId(), user.getName());
    }
}
