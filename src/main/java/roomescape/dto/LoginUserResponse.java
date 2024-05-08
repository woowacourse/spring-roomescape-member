package roomescape.dto;

import roomescape.domain.LoginUser;

public record LoginUserResponse(long id, String name, String email) {
    public static LoginUserResponse from(LoginUser loginUser) {
        return new LoginUserResponse(
                loginUser.getId(),
                loginUser.getName(),
                loginUser.getEmail()
        );
    }
}
