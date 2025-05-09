package roomescape.dto;

import roomescape.entity.Member;
import roomescape.entity.MemberRole;

public record MemberRequest(String email, String password, String name) {

    public Member toMember(MemberRole role) {
        return new Member(name, email, password, role);
    }
}
