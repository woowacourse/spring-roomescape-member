package roomescape.controller.api.dto.response;

import java.util.List;
import roomescape.service.dto.output.MemberOutput;

public record MembersResponse(List<MemberResponse> members) {

    public static MembersResponse from(final List<MemberOutput> outputs) {
        return new MembersResponse(MemberResponse.list(outputs));
    }
}
