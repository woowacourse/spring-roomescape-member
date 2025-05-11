package roomescape.controller.dto.member;

import roomescape.entity.Member;

public class MemberResponse {

    private final long id;
    private final String name;
    private final String email;
    private final String role;

    private MemberResponse(
            final Long id,
            final String name,
            final String email,
            final String role
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole().toString()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
