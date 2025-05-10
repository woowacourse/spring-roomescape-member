package roomescape.auth.dto;

import roomescape.auth.domain.LoginMember;

public record CheckLoginResponse(String name) {

    public static CheckLoginResponse from(final LoginMember loginMember) {
        return new CheckLoginResponse(loginMember.name());
    }
}
