package roomescape.dto.login;

import roomescape.domain.Member;
import roomescape.entity.MemberEntity;

public record TokenRequest(String password, String email) {

    public Member toMember() {
        return new Member(null,null,email,password);
    }
}
