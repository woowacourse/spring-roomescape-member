package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberResponse(
    Long id,
    String name
) {

    public static MemberResponse from(Member user) {
        return new MemberResponse(
            user.getId(),
            user.getName()
        );
    }
}
