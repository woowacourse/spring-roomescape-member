package roomescape.service.dto.login;

import roomescape.domain.member.Member;

public record MemberNameResponse(String name) {

    public static MemberNameResponse of(Member member) {
        return new MemberNameResponse(member.getName());
    }
}
