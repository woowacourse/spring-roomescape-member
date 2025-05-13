package roomescape.controller.member.dto;

import roomescape.model.Member;

public record MemberResponseDto(
        Long id,
        String name
) {
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(member.getId(), member.getName());
    }
}
