package roomescape.model;

import roomescape.dto.response.MemberResponseDto;

public record LoginMember(
        Long id,
        String name,
        String email,
        Role role
) {
    public LoginMember(MemberResponseDto memberResponseDto) {
        this(
                memberResponseDto.id(),
                memberResponseDto.name(),
                memberResponseDto.email(),
                memberResponseDto.role()
        );
    }
}
