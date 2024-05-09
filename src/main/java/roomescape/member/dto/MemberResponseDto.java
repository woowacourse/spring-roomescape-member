package roomescape.member.dto;

import roomescape.member.domain.Member;

public record MemberResponseDto(Long id, String name, String email, String password, String role) {

    public MemberResponseDto(Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }
}
