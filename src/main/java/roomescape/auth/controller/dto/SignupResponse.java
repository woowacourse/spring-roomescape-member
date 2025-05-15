package roomescape.auth.controller.dto;

import roomescape.entity.Member;

public class SignupResponse {
    private final long id;
    private final String name;
    private final String email;

    private SignupResponse(final long id, final String name, final String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static SignupResponse from(Member member) {
        return new SignupResponse(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
