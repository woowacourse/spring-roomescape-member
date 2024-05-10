package roomescape.fixture;

import roomescape.domain.user.Member;
import roomescape.service.dto.input.MemberCreateInput;

public class MemberFixture {
    public static MemberCreateInput getCreateInput() {
        return new MemberCreateInput("조이썬",
                "joyson5582@gmail.com",
                "password1234");
    }

    public static Member getDomain() {
        return Member.from(
                null,
                "조이썬",
                "joyson5582@gmail.com",
                "password1234"
        );
    }
}
