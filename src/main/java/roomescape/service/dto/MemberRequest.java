package roomescape.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Member;

public record MemberRequest(@NotNull long id,
                            @NotBlank String name,
                            @Email String email,
                            @NotBlank String password,
                            String role) {
    public Member toLoginMember(MemberRequest memberRequest) {
        return new Member(memberRequest.id, memberRequest.name, memberRequest.email,
                memberRequest.password,
                memberRequest.role());
    }
}
