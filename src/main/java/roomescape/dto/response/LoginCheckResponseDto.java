package roomescape.dto.response;

import roomescape.domain.Member;

public class LoginCheckResponseDto {
    private String name;

    public LoginCheckResponseDto() {
    }

    public LoginCheckResponseDto(final Member member) {
        this(member.getName());
    }

    public LoginCheckResponseDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
