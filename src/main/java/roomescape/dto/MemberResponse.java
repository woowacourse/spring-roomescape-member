package roomescape.dto;

import roomescape.domain.Member;

public record MemberResponse(long memberId, String name) {

    public static MemberResponse of(final Member member) {
        return new MemberResponse(member.getId(), member.getName());
    }
}
