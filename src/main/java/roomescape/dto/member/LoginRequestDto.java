package roomescape.dto.member;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

public record LoginRequestDto(@NotNull String email, @NotNull String password) {

    public Member toEntity() {
        return new Member(null, email, password, null, Role.USER);
    }
}
