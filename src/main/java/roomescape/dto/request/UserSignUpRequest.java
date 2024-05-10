package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.Member;
import roomescape.domain.Role;

public record UserSignUpRequest(
        @NotNull
        String name,
        @NotNull
        String email,
        @NotNull
        String password
) {
    public Member toEntity() {
        return new Member(name, email, password, Role.USER);
    }
}
