package roomescape.service.result;

import roomescape.domain.Member;

public record CheckLoginUserResult(String name) {

    public static CheckLoginUserResult from(Member member) { //TODO: from으로 할지, of로 할지
        return new CheckLoginUserResult(member.getName());
    }
}
