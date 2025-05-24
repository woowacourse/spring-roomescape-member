package roomescape.fixture;

import java.util.List;
import roomescape.domain.LoginMember;
import roomescape.domain.Role;
import roomescape.repository.FakeMemberRepository;

public class FakeMemberRepositoryFixture {

    public static FakeMemberRepository create() {
        return new FakeMemberRepository(List.of(
                new LoginMember(1L, "어드민", "admin@gmail.com", "wooteco7", Role.ADMIN),
                new LoginMember(2L, "회원", "user@gmail.com", "wooteco7", Role.USER)
        ));
    }
}
