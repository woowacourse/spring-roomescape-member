package roomescape.member.service.dto.response;

import roomescape.member.domain.Member;

public record MemberServiceResponse(
        Long id,
        String name
) {

    public static MemberServiceResponse from(Member member) {
        return new MemberServiceResponse(member.getId(), member.getName());
    }
}
