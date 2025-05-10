package roomescape.member.dto.response;

import roomescape.global.auth.LoginMember;

public class AuthResponse {

    public record LoginResponse(
            String name
    ) {
        public static LoginResponse from(LoginMember loginMember) {
            return new LoginResponse(
                    loginMember.name()
            );
        }
    }
}
