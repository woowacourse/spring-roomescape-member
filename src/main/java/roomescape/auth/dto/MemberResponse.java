package roomescape.auth.dto;

import roomescape.domain.Member;

public record MemberResponse(Long id, String name) {
    public static MemberResponse from(Member savedMember) {
        return new MemberResponse(savedMember.getId(), savedMember.getName());
    }
}
