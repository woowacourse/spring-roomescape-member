package roomescape.service.result;

import roomescape.domain.Member;

public record MemberResult(Long id, String name, String email) {

    public static MemberResult from(Member member) {
        return new MemberResult(member.getId(), member.getName(), member.getEmail());
    }
}
