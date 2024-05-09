package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberResponse(String email, String name) {

    public MemberResponse(Member member) {
        this(member.getEmail().getEmail(), member.getName().getName());
    }
}
