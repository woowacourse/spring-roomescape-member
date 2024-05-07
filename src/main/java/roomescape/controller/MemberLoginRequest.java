package roomescape.controller;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @NotBlank
//        @Email //TODO 이거 써보기
        String email,

        @NotBlank
        String password
) {
}
