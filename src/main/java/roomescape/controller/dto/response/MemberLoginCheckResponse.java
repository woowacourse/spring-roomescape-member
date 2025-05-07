package roomescape.controller.dto.response;

import roomescape.service.dto.response.MemberLoginCheckResult;

public record MemberLoginCheckResponse(String name) {
    public static MemberLoginCheckResponse from(final MemberLoginCheckResult memberLoginCheckResult) {
        return new MemberLoginCheckResponse(memberLoginCheckResult.name());
    }
}
