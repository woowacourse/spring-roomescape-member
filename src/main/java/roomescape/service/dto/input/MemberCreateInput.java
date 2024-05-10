package roomescape.service.dto.input;

import roomescape.domain.user.Member;

public record MemberCreateInput(String name, String email, String password) {
    public Member toMember() {
        return Member.fromMember(null, name, email, password);
    }
}
