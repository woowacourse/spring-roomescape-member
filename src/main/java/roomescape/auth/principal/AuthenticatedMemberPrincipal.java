package roomescape.auth.principal;

import roomescape.member.model.Member;
import roomescape.member.model.MemberRole;

public record AuthenticatedMemberPrincipal(
        Long id,
        String name,
        String email,
        MemberRole role
) {
    public static AuthenticatedMemberPrincipal from(final Member member) {
        return new AuthenticatedMemberPrincipal(
                member.getId(),
                member.getName().value(),
                member.getEmail().value(),
                member.getRole()
        );
    }
}
