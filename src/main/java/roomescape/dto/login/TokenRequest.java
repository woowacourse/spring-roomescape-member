package roomescape.dto.login;

import roomescape.entity.MemberEntity;

public record TokenRequest(String password, String email) {

    public MemberEntity toMemberEntity() {
        return MemberEntity.of(null,null,email,password);
    }
}
