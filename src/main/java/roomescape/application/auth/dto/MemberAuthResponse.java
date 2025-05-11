package roomescape.application.auth.dto;

import roomescape.application.dto.MemberDto;

public record MemberAuthResponse(String name) {

    public static MemberAuthResponse from(MemberDto memberDto) {
        return new MemberAuthResponse(memberDto.name());
    }
}
