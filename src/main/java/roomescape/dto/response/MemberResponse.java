package roomescape.dto.response;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record MemberResponse(Long id, String name, String email, Role role) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.id(), member.name(), member.email(), member.role());
    }
}
