package roomescape.dto;

import roomescape.domain.Member;

public record SignUpResponse(long id, String name) {

    public static SignUpResponse of(final Member member) {
        return new SignUpResponse(member.getId(), member.getName());
    }
}
