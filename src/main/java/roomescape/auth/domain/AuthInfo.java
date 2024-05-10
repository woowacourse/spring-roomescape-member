package roomescape.auth.domain;

import roomescape.member.domain.MemberRole;

public class AuthInfo {
    private final Long memberId;
    private final String name;
    private final MemberRole memberRole;

    public AuthInfo(final Long memberId, final String name, final MemberRole memberRole) {
        this.memberId = memberId;
        this.name = name;
        this.memberRole = memberRole;
    }

    public boolean isNotAdmin() {
        return this.memberRole.isNotAdmin();
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }
}
