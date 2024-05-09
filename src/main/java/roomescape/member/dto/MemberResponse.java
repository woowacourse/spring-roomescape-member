package roomescape.member.dto;

import roomescape.member.model.Member;

public record MemberResponse(String name) {
    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getName().value());
    }
}
