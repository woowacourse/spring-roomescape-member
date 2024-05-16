package roomescape.dto.request;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record MemberCreateRequest(String name, String email, String password) {
    public Member createMember() {
        return new Member(name, email, password, Role.MEMBER);
    }
}
