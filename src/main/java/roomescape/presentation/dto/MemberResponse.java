package roomescape.presentation.dto;

import roomescape.business.domain.member.Member;

public record MemberResponse(
        long id,
        String email,
        String name
) {

    public MemberResponse(final Member member) {
        this(member.getId(), member.getEmail(), member.getName());
    }
}
