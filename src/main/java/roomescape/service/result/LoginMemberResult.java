package roomescape.service.result;

import roomescape.domain.Member;

public record LoginMemberResult(Long id, String name, String email, String password) {

    public static LoginMemberResult from(final Member member) {
        return new LoginMemberResult(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }
}
