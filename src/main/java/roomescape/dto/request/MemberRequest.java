package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import roomescape.domain.member.Member;

public record MemberRequest(
        @PositiveOrZero Long id,
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String role
) {
    public MemberRequest(Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }
}
