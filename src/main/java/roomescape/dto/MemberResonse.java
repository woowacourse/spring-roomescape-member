package roomescape.dto;

import roomescape.domain.Member;

public record MemberResonse(Long id, String name) {

    public static MemberResonse from(final Member member) {
        return new MemberResonse(member.getId(), member.getName());
    }
}
