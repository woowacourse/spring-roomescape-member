package roomescape.dto.response;

import roomescape.domain.User;

public record MemberResponse(
        Long id,
        String name
) {

    public static MemberResponse from(User user) {
        return new MemberResponse(user.id(), user.name());
    }
}
