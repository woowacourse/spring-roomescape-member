package roomescape.global.auth.dto;

import roomescape.global.auth.domain.LoginMember;

public record CheckLoginResponse(String name) {

    public static CheckLoginResponse from(final LoginMember loginMember) {
        return new CheckLoginResponse(loginMember.name());
    }
}
