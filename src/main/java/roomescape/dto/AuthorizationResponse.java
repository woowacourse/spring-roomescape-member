package roomescape.dto;

import roomescape.entity.Member;

public record AuthorizationResponse(
        String name
) {
    public AuthorizationResponse(Member member) {
        this(member.getName());
    }
}
