package roomescape.service.dto;

import roomescape.domain.Member;

public record MemberResponse(long id, String name, String email, String password, String role) {
    public MemberResponse(final Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }
}
