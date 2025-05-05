package roomescape.member.presentation.dto.response;

import java.util.List;
import roomescape.member.application.dto.info.MemberDto;

public record MemberResponse(
        Long id,
        String name,
        String email
) {

    public static MemberResponse from(MemberDto dto) {
        return new MemberResponse(dto.id(), dto.name(), dto.email());
    }
    public static List<MemberResponse> from(List<MemberDto> dtos) {
        return dtos.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
