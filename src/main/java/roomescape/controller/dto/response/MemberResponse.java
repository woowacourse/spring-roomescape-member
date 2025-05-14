package roomescape.controller.dto.response;

import roomescape.service.dto.response.MemberResult;

public record MemberResponse(long id, String name) {
    public static MemberResponse from(MemberResult memberResult) {
        return new MemberResponse(memberResult.id(), memberResult.name());
    }
}
