package roomescape.service.dto.response;

import roomescape.domain.Member;

public record MemberSignUpResult(String name) {
    public static MemberSignUpResult from(final Member savedMember) {
        return new MemberSignUpResult(savedMember.getName());
    }
}
