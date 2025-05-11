package roomescape.dto;

import roomescape.domain.Member;

public record MemberNameResponse(String name) {

    public static MemberNameResponse from(final Member member) {
        return new MemberNameResponse(
                member.getName()
        );
    }

}
