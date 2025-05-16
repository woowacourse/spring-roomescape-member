package roomescape.auth.controller.dto;

import roomescape.member.domain.Member;

public record CheckLoginResponse(String name) {

    public static CheckLoginResponse from(final Member member) {
        return new CheckLoginResponse(member.getName());
    }
}
