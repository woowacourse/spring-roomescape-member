package roomescape.service.result;

import roomescape.domain.Member;

public record RegisterUserResult(Long id, String email, String password, String name) {

    public static RegisterUserResult from(Member member) {
        return new RegisterUserResult(member.getId(), member.getEmail(), member.getPassword(), member.getName());
    }
}
