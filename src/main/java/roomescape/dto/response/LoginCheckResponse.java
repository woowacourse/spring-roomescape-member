package roomescape.dto.response;

import roomescape.domain.Member;

public record LoginCheckResponse(
        String name
) {

    public static LoginCheckResponse from(Member member) {
        return new LoginCheckResponse(member.name());
    }
}
