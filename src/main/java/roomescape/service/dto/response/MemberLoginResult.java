package roomescape.service.dto.response;

import roomescape.domain.Member;
import roomescape.domain.MemberRoleType;

public record MemberLoginResult(long id, String email, String password, MemberRoleType role) {

    public static MemberLoginResult from(Member member) {
        return new MemberLoginResult(member.getId(), member.getEmail(), member.getPassword(), member.getRole());
    }
}
