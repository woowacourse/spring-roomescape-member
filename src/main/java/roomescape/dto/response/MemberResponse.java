package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberResponse(Long id, String name, String email) {

    public MemberResponse(final Member member) {
        this(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }
}
