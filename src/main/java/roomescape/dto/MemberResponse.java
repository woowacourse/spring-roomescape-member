package roomescape.dto;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record MemberResponse(Long id, String name, Role role) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName().getValue(),
                member.getRole()
        );
    }

    public static MemberResponse from(LoginMember loginMember) {
        return new MemberResponse(
                loginMember.id(),
                loginMember.name(),
                loginMember.role()
        );
    }
}
