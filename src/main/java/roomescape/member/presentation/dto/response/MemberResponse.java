package roomescape.member.presentation.dto.response;

import java.util.List;
import roomescape.member.application.dto.MemberDto;

public record MemberResponse(
        String name
) {

    public static MemberResponse from(MemberDto dto) {
        return new MemberResponse(dto.name());
    }
    public static List<MemberResponse> from(List<MemberDto> dtos) {
        return dtos.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
