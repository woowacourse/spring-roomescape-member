package roomescape.dto.request;

import roomescape.model.Member;
import roomescape.model.MemberName;
import roomescape.model.Role;

public record LoginMember(Long id, Role role, MemberName name, String email, String password) {
    public Member toEntity() {
        return new Member(id, role, name, email, password);
    }

    public static LoginMember from(Member member) {
        return new LoginMember(member.getId(), member.getRole(), member.getMemberName(), member.getEmail(),
                member.getPassword());
    }
}
