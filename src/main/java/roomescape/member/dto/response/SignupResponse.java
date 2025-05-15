package roomescape.member.dto.response;

import roomescape.member.domain.Member;

public record SignupResponse(Long id) {

    public static SignupResponse from(final Member member) {
        return new SignupResponse(member.getId());
    }
}
