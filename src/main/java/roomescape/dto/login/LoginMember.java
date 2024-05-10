package roomescape.dto.login;

import roomescape.domain.member.Member;

public record LoginMember(
        Long id
) {

    public static LoginMember from(Member member) {
        return new LoginMember(member.getId());
    }
}
