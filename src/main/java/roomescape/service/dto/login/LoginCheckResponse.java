package roomescape.service.dto.login;

import roomescape.domain.Member;

public record LoginCheckResponse(String name) {

    public static LoginCheckResponse from(Member member) {
        return new LoginCheckResponse(member.getName());
    }
}
