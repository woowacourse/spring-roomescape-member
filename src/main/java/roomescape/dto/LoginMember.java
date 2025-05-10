package roomescape.dto;

import roomescape.model.Member;
import roomescape.model.MemberName;
import roomescape.model.Role;

public record LoginMember(Long id, Role role, MemberName name, String email, String password) {
    public Member toEntity() {
        return new Member(id, role, name, email, password);
    }
}
