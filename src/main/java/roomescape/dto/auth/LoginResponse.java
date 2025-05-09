package roomescape.dto.auth;

import roomescape.entity.Member;

public record LoginResponse(
        String name
) {

    public static LoginResponse from(final Member member) {
        return new LoginResponse(member.getName());
    }
}
