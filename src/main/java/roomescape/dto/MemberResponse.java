package roomescape.dto;

import roomescape.domain.Member;

public record MemberResponse(
    String name
) {

    public static MemberResponse from(Member user) {
        return new MemberResponse(
            user.getName()
        );
    }
}
