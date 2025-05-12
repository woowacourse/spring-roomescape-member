package roomescape.auth.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import roomescape.auth.entity.Member;
import roomescape.auth.entity.Role;

public record LoginMember(
        @NotNull
        Long id,
        @NotEmpty
        String name,
        @Email
        String email,
        @NotNull
        Role role
) {
    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
