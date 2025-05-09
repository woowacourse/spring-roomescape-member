package roomescape.dto.response;

import roomescape.domain.Member;

public record CheckResponseDto(String name) {

    public static CheckResponseDto from(Member member) {
        return new CheckResponseDto(member.getName());
    }
}
