package roomescape.member.dto;

import roomescape.member.domain.Member;

public record MemberResponseDto(
        long id,
        String name,
        String email
) {
    public MemberResponseDto(final Member member) {
        this(member.getId(), member.getName(), member.getEmail());
    }
}
