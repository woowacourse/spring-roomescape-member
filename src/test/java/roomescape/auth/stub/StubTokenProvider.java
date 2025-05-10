package roomescape.auth.stub;

import roomescape.auth.infrastructure.jwt.TokenProvider;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public class StubTokenProvider implements TokenProvider {

    public static final Long STUB_MEMBER_ID = 1L;

    @Override
    public String issue(Member member) {
        if (member.getRole() == Role.USER) {
            return "user_stub_token";
        }
        return "admin_stub_token";
    }

    @Override
    public boolean isAdmin(String token) {
        return token.equals("admin_stub_token");
    }

    @Override
    public Long getMemberId(String token) {
        return STUB_MEMBER_ID;
    }
}
