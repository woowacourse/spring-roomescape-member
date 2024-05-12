package roomescape.dto.response;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record MemberResponse(Long id, String email, String name, Role role) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getRole()
        );
    }
}
