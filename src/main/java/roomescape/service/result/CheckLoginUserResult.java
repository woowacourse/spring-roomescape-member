package roomescape.service.result;

import roomescape.domain.Member;

public record CheckLoginUserResult(String name) {

    public static CheckLoginUserResult from(Member member) {
        return new CheckLoginUserResult(member.getName());
    }
}
