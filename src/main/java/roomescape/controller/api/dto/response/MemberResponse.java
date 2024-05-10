package roomescape.controller.api.dto.response;

import roomescape.service.dto.output.MemberOutput;

public record MemberResponse(long id, String name) {

    public static MemberResponse from(final MemberOutput member) {
        return new MemberResponse(member.id(), member.name());
    }
}
