package roomescape.controller.api.dto.response;

import java.util.List;
import roomescape.service.dto.output.MemberOutput;

public record MemberResponse(long id, String name, String role) {

    public static MemberResponse from(final MemberOutput member) {
        return new MemberResponse(member.id(), member.name(), member.role());
    }

    public static List<MemberResponse> list(final List<MemberOutput> outputs) {
        return outputs.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
