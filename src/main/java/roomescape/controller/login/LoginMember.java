package roomescape.controller.login;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record LoginMember(
        Long id,
        String name,
        String email,
        Role role
) {
    public static LoginMember from(Member member) {
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
