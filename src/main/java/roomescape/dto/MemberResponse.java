package roomescape.dto;

import roomescape.model.Member;

public record MemberResponse(Long id, String name, String role, String email) {

    public MemberResponse(final Member member) {
        this(member.getId(), member.getName(), member.getRole().name(), member.getEmail());
    }
}
