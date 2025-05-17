package roomescape.auth;

import jakarta.validation.constraints.NotBlank;
import roomescape.member.domain.Member;

public record LoginMember(
        @NotBlank Long id,
        @NotBlank String email,
        @NotBlank String name,
        @NotBlank String role
) {

    public static LoginMember from(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getName(), member.getRole().name());
    }
}
