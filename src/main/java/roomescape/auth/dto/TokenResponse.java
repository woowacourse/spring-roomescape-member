package roomescape.auth.dto;

import roomescape.member.domain.Member;

public record TokenResponse(String name) {

    public TokenResponse(Member member) {
        this(member.getName());
    }
}
