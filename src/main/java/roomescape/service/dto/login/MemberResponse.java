package roomescape.service.dto.login;

import roomescape.domain.member.Member;

public record MemberResponse(String name) {

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getName());
    }
}
