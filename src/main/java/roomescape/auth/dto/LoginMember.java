package roomescape.auth.dto;

import roomescape.member.domain.Member;

public record LoginMember(String name) {
    public static LoginMember from(Member member) {
        return new LoginMember(member.getName().getValue());
    }
}
