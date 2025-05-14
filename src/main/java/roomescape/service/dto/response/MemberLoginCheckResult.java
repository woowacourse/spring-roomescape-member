package roomescape.service.dto.response;

import roomescape.domain.MemberRoleType;
import roomescape.jwt.JwtRequest;

public record MemberLoginCheckResult(long id, String name, MemberRoleType role) {

    public static MemberLoginCheckResult from(JwtRequest jwtRequest) {
        return new MemberLoginCheckResult(jwtRequest.id(), jwtRequest.name(), jwtRequest.role());
    }
}
