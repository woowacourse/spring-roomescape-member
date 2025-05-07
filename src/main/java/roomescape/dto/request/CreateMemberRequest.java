package roomescape.dto.request;

import roomescape.domain.Member;

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
                this.password
        );
    }
}
