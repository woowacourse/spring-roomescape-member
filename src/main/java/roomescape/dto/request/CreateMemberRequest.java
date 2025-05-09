package roomescape.dto.request;

import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

public record CreateMemberRequest(
        String name,
        String email,
        String password
) {
    public Member toEntity() {
        return new Member(
                null,
                this.name,
                this.email,
                this.password,
                Role.MEMBER
        );
    }
}
