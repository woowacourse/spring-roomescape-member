package roomescape.member.dto;

import roomescape.member.domain.Member;

public record MemberResponse(Long id, String name, String email, String role) {

    public MemberResponse(Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
