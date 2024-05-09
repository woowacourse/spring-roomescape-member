package roomescape.dto.request;

import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;

public record SignupRequest(
        String name,
        String email,
        String password
) {
    public Member toEntity() {
        return new Member(new MemberName(name), email, password);
    }
}
