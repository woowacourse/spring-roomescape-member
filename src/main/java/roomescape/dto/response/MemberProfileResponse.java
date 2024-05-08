package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberProfileResponse(String name) {
    public static MemberProfileResponse fromMember(Member member) {
        return new MemberProfileResponse(member.getNameValue());
    }
}
