package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberNameResponse(String name) {
    public static MemberNameResponse fromMember(Member member) {
        return new MemberNameResponse(member.getNameValue());
    }
}
