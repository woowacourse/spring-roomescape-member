package roomescape.global.dto;

import roomescape.domain.member.model.Member;

public record CheckResponseDto(String name) {

    public static CheckResponseDto from(Member member) {
        return new CheckResponseDto(member.getName());
    }
}
