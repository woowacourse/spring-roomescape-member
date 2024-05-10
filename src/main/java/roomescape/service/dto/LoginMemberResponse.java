package roomescape.service.dto;

import roomescape.domain.LoginMember;

public record LoginMemberResponse(long id, String name, String email, String password, String role) {
    public LoginMemberResponse(final LoginMember member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }
}
