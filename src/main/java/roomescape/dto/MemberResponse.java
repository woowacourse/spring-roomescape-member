package roomescape.dto;

import roomescape.domain.Member;

public record MemberResponse(Long id, String name, String email, String role) {

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getName(),member.getEmail(), member.getRole());
    }
}
