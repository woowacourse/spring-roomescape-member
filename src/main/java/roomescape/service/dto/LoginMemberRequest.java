package roomescape.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.LoginMember;

public record LoginMemberRequest(@NotNull long id,
                                 @NotBlank String name,
                                 @Email String email,
                                 @NotBlank String password,
                                 String role) {
    public LoginMember toLoginMember(LoginMemberRequest loginMemberRequest) {
        return new LoginMember(loginMemberRequest.id, loginMemberRequest.name, loginMemberRequest.email,
                loginMemberRequest.password,
                loginMemberRequest.role());
    }
}
