package roomescape.member.ui.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateMemberRequest(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name
) {

}
