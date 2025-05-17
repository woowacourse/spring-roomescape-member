package roomescape.fixture;

import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;
import roomescape.member.domain.Password;

public class Fixture {

    public Member getNomalMember() {
        return Member.builder()
                .id(1L)
                .name("멤버")
                .email("email1")
                .password(Password.createForMember("password"))
                .role(MemberRole.MEMBER)
                .build();
    }
}
