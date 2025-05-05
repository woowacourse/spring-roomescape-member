package roomescape.dto.member;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

public record SignupRequestDto(@NotNull String username, @NotNull String password) {

    public Member toEntity() {
        return new Member(null, username, password, Role.USER);
    }
}
