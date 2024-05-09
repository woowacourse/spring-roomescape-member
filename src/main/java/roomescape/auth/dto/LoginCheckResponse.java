package roomescape.auth.dto;

import roomescape.member.model.Member;

public record LoginCheckResponse(String name) {

    public static LoginCheckResponse from(final Member member) {
        return new LoginCheckResponse(member.getName().value());
    }
}
