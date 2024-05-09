package roomescape.member.dto;

import roomescape.member.model.Member;
import roomescape.member.model.MemberRole;

public record SaveMemberRequest(
        MemberRole role,
        String email,
        String name,
        String password
) {
    public Member toMember(final String encodedPassword) {
        return Member.createMemberWithoutId(
                role, encodedPassword, name, email
        );
    }
}
