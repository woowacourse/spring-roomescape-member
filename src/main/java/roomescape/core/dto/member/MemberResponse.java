package roomescape.core.dto.member;

import roomescape.core.domain.Member;

public class MemberResponse {
    private String name;

    public MemberResponse() {
    }

    public MemberResponse(final Member member) {
        this(member.getName());
    }

    public MemberResponse(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
