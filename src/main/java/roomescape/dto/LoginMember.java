package roomescape.dto;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record LoginMember(Long id, String name, Role role) {
    public static LoginMember from(Member member) {
        return new LoginMember(
                member.getId(),
                member.getName().getValue(),
                member.getRole()
        );
    }
}
