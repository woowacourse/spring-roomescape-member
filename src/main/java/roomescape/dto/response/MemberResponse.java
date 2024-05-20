package roomescape.dto.response;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record MemberResponse(Long id, Role role, String name) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getRole(), member.getName());
    }
}
