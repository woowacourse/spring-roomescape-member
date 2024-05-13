package roomescape.member.dto;

import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public record MemberResponse(Long id, String name, String email, Role role) {

    public MemberResponse(Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
