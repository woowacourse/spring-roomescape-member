package roomescape.fixture;

import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberPassword;
import roomescape.dto.member.MemberNameResponse;

public class MemberFixtures {

    private MemberFixtures() {
    }

    public static Member createMember(long id, String name, String email, String password) {
        return new Member(id, new MemberName(name), new MemberEmail(email), new MemberPassword(password));
    }

    public static Member createMember(String name) {
        return new Member(null, new MemberName(name), new MemberEmail("test@test.com"), new MemberPassword("default"));
    }

    public static MemberNameResponse createNameResponse(String name) {
        return new MemberNameResponse(createMember(1L, name, "test@test.com", "1234"));
    }
}
