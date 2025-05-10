package roomescape.dto.response;

import roomescape.domain.Member;

public record LoginMemberResponse(
        Long id,
        String name,
        String email
) {
    public static LoginMemberResponse from(Member member) {
        return new LoginMemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
