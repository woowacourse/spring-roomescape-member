package roomescape.member.ui.dto;

import roomescape.member.domain.Member;

public record MemberResponse(
        Long id,
        String name,
        String email

) {

    public record Name(
            String name
    ) {
        public static Name from(final Member member) {
            return new Name(member.getName());
        }
    }
}
