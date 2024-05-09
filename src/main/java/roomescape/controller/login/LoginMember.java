package roomescape.controller.login;

import roomescape.service.dto.MemberResponse;
import roomescape.domain.Role;

public record LoginMember(
        Long id,
        String name,
        Role role
) {
    public MemberResponse toMemberResponse() {
        return new MemberResponse(id, name, role.name());
    }
}
