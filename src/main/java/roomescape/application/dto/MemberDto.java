package roomescape.application.dto;

import roomescape.domain.Member;

public record MemberDto(
        Long id,
        String name,
        String email,
        String password
) {
    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }
}
