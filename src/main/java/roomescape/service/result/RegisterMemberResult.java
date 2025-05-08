package roomescape.service.result;

import roomescape.domain.Member;

public record RegisterMemberResult(Long id, String name, String email, String password) {

    public static RegisterMemberResult from(Member member) {
        return new RegisterMemberResult(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }
}
