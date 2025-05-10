package roomescape.member.service.dto;

import roomescape.member.domain.Member;

public record LoginMemberInfo(long id, String name, String email, String password) {
    public LoginMemberInfo(Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }
}
