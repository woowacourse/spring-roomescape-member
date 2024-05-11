package roomescape.member.dto;

import roomescape.member.domain.LoginMember;

// TODO: MemberResponse에 편입할 수 있지 않을까? 테이블에 저장된 id값은 굳이 숨겨야 하는 정보가 아니니까.
public record MemberNameResponse(String name) {

    public MemberNameResponse(LoginMember loginMember) {
        this(loginMember.getName().name());
    }
}
