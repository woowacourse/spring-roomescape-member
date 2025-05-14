package roomescape.dto.response;

import roomescape.domain.Member;

public record LoginCheckResponse(
        String name
) {

    private static final String NOT_LOGGED_IN_USER = "비로그인 사용자";

    public static LoginCheckResponse from(Member member) {
        if (member == null) {
            return new LoginCheckResponse(NOT_LOGGED_IN_USER);
        }
        return new LoginCheckResponse(member.name());
    }
}
