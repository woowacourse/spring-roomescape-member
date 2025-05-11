package roomescape.dto.response;

import roomescape.domain.Member;

public record MemberSignUpResponse(
        String email,
        boolean isSuccess
) {
    public static MemberSignUpResponse of(Member member, boolean isSuccess) {
        return new MemberSignUpResponse(member.getEmail(), isSuccess);
    }
}
