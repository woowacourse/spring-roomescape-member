package roomescape.presentation.dto;

import roomescape.domain.Member;

public record LoginMember(String name) {

    public static LoginMember from(Member member) {
        return new LoginMember(member.getName());
    }
}
