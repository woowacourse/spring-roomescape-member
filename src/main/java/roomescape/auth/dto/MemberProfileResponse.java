package roomescape.auth.dto;

import roomescape.member.domain.Member;

public record MemberProfileResponse(String name) {

    public static MemberProfileResponse from(Member member) {
        return new MemberProfileResponse(member.getName());
    }

}
