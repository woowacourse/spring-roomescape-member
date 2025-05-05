package roomescape.dto.member;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

public record SignupRequestDto(@NotNull String email, @NotNull String password, @NotNull String name) {

    public Member toEntity() {
        return new Member(null, email, password, name, Role.USER);
    }
}
