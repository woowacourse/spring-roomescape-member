package roomescape.dto.response;

import roomescape.domain.Member;

public record LoginMember(
        Long id,
        String email,
        String name
) {
    public Member toMember() {
        return new Member(id, email, null, name);
    }
}
