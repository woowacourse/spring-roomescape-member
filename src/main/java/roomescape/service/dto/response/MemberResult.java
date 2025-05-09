package roomescape.service.dto.response;

import roomescape.domain.Member;

public record MemberResult(long id, String name) {
    public static MemberResult from(Member member) {
        return new MemberResult(member.getId(), member.getName());
    }
}
