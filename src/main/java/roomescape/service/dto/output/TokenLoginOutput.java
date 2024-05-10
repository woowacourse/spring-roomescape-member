package roomescape.service.dto.output;

import roomescape.domain.user.Member;

public record TokenLoginOutput(long id, String name, String email, String password) {
    public static TokenLoginOutput toOutput(final Member member) {
        return new TokenLoginOutput(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }
}
