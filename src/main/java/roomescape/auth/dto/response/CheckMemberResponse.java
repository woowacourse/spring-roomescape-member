package roomescape.auth.dto.response;

import roomescape.member.domain.Member;

public record CheckMemberResponse(String name) {
    public static CheckMemberResponse from(final Member member) {
        return new CheckMemberResponse(member.getName());
    }
}
