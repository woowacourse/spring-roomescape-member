package roomescape.service.dto;

import roomescape.domain.Member;

public class MemberResponseDto {

    private final String name;

    public MemberResponseDto(Member member) {
        this.name = member.getName();
    }

    public String getName() {
        return name;
    }
}
