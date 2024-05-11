package roomescape.auth.service.dto;

import roomescape.member.domain.Member;

public record LoginCheckResponse(String name) {
    public LoginCheckResponse(Member member) {
        this(member.getMemberName());
    }
}
