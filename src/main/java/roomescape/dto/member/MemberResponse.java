package roomescape.dto.member;

import roomescape.domain.member.Member;

public record MemberResponse(long id, String name, String email, String password) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName().getValue(),
                member.getEmail(),
                member.getPassword()
        );
    }
}
