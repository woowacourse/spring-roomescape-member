package roomescape.dto.auth;

import roomescape.domain.member.Member;

public record LoginResponse(String name) {

    public static LoginResponse from(Member member) {
        return new LoginResponse(member.getName());
    }
}
