package roomescape.dto.response;

import roomescape.domain.Member;

public class MemberResponseDto {
    private Long id;
    private String name;
    private String email;
    private String role;

    public MemberResponseDto() {
    }

    public MemberResponseDto(final Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getRoleName());
    }

    public MemberResponseDto(
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
