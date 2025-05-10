package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberNameSelectResponse(
        Long id,
        String name
) {
    public static MemberNameSelectResponse from(Member member) {
        return new MemberNameSelectResponse(member.getId(), member.getName());
    }
}
