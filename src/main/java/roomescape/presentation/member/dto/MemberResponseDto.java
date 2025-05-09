package roomescape.presentation.member.dto;

import roomescape.business.domain.member.Member;

public record MemberResponseDto(
        Long id,
        String name
) {

    public static MemberResponseDto toResponse(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getName()
        );
    }
}
