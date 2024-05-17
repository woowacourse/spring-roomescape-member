package roomescape.member.dto;

import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public record MemberResponse(Long id, String name, Role role) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName().getValue(),
                member.getRole()
        );
    }
}
