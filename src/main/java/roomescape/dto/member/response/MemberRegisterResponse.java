package roomescape.dto.member.response;

import roomescape.domain.member.Member;

public record MemberRegisterResponse(
        Long id,
        String name,
        String email
) {
    public static MemberRegisterResponse from(final Long id, final Member member) {
        return new MemberRegisterResponse(id, member.getName(), member.getEmail());
    }
}
