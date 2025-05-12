package roomescape.member.dto.response;

import roomescape.auth.domain.Role;
import roomescape.member.domain.Member;

public record MemberResponse(
        Long id,
        String email,
        String name,
        Role role
) {
    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getName(), member.getRole());
    }
}
