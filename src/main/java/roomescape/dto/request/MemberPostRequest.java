package roomescape.dto.request;

import roomescape.entity.Member;
import roomescape.entity.MemberRole;

public record MemberPostRequest(
        String email,
        String password,
        String name) {

    public Member toMember(MemberRole role) {
        return new Member(name, email, password, role);
    }
}
